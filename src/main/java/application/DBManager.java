package application;
import application.src.Ticket;
import application.src.User;
import application.src.UserTicket;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.push;

public class DBManager {
    private static MongoDatabase database = null;
    private static MongoCollection<Document> usersCollection = null;
    private static MongoCollection<Document> ticketTypesCollection = null;
    private static MongoCollection<Document> ticketsCollection = null;
    static {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClient mongoClient = MongoClients.create(connectionString);
        try {
            // Get the database
            database = mongoClient.getDatabase("ApplicationDatabase"); // Replace "yourDatabaseName" with your actual database name
            usersCollection = database.getCollection("users");
            ticketTypesCollection = database.getCollection("ticketTypes");
            ticketsCollection = database.getCollection("tickets");
        } catch (Exception ignored) {
            System.exit(0);
        }
    }
    private DBManager() {
    }


    public static int createUser(String userName, String password, String phoneNumber) {
        // Перевірка наявності користувача з вказаним user_name
        Document existingUser = usersCollection.find(new Document("user_name", userName)).first();
        if (existingUser != null) {
            // Користувач з таким user_name вже існує
            return 1;
        }
        // Створення документа для нового користувача
        Document newUser = new Document("user_name", userName)
                .append("password", password)
                .append("phone_number", phoneNumber)
                .append("tickets", new ArrayList<String>());

        try {
            // Вставка документа в колекцію
            usersCollection.insertOne(newUser);
            return 0; // Успішно створено нового користувача
        } catch (MongoException e) {
            e.printStackTrace();
            return 2; // Помилка під час вставки документа
        }
    }


    public static int loginUser (String userName, String password) {
        try {
            // Пошук користувача за userName
            Document userDocument = usersCollection.find(eq("user_name", userName)).first();

            if (userDocument == null) {
                // Немає такого користувача
                return 1;
            }

            // Отримання зашифрованого паролю з бази даних
            String storedPassword = userDocument.getString("password");

            // Перевірка введеного паролю
            if (!password.equals(storedPassword)) {
                // Неправильний пароль
                return 2;
            }
            List<Document> userTickets = userDocument.getList("tickets", Document.class);
            List<UserTicket> userTicketsPacked = new ArrayList<UserTicket>();
                // Ітерація по кожному документу в масиві 'tickets'
            for (Document ticket : userTickets) {
                userTicketsPacked.add(new UserTicket(ticket.getObjectId("ticket_id"), ticket.getString("duration"),
                        ticket.getString("food"), ticket.getString("transport")));
            }
            User.setUser(userDocument.getObjectId("_id"), userDocument.getString("user_name"), userDocument.getString("password"),
                    userDocument.getString("phone_number"), userTicketsPacked);
            // Все добре
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 3; // Сталась помилка
        }
    }


    public static List<String> getTicketTypes () {
        List<String> ticketTypes = new ArrayList<>();

        // Create a MongoDB client
        try {
            for (Document doc : ticketTypesCollection.find()) {
                // Assuming each document has a field called "type" that we want to extract
                String name = doc.getString("name");
                if (name != null) {
                    ticketTypes.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ticketTypes;
    }

    public static int createTicket (String ticketType, String ticketName, String description, double price,
                                 byte [] byteImage, List<String> durationOptions, List<String> foodOptions, List<String> transportOptions) {
        try {
            Document ticket = new Document()
                    .append("ticket_type", ticketType)
                    .append("ticket_name", ticketName)
                    .append("description", description)
                    .append("price", price)
                    .append("image", new Binary(byteImage))
                    .append("duration_options", durationOptions)
                    .append("food_options", foodOptions)
                    .append("transport_options", transportOptions);

            ticketsCollection.insertOne(ticket);
            return 0; // Успішно додано
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // Сталася помилка
        }
    }
    public static List<Ticket> getAllTickets(String typeFilter) {
        try {
            List<Document> tickets;
            if (typeFilter == null) {
                tickets = ticketsCollection.find().into(new ArrayList<>());
            }else {
                tickets = ticketsCollection.find(eq("ticket_type", typeFilter)).into(new ArrayList<>());
            }
            List<Ticket> ticketObjects = new ArrayList<Ticket>();
            // Виведення інформації з кожного документа
            for (Document ticket : tickets) {
                byte[] image = ticket.get("image", Binary.class).getData();
                ticketObjects.add(new Ticket(ticket.getObjectId("_id"), ticket.getString("ticket_type"),
                        ticket.getString("ticket_name"), ticket.getString("description"), ticket.getDouble("price"), image,
                        ticket.getList("duration_options", String.class), ticket.getList("food_options", String.class),
                        ticket.getList("transport_options", String.class)));
            }
            return ticketObjects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Ticket> getAllUserTickets() {
        try {
            List<ObjectId> ticketIds = new ArrayList<ObjectId>();
            List<UserTicket> userTickets = User.getTickets();
            for (UserTicket userTicket: userTickets) {
                ticketIds.add(userTicket.getTicketId());
            };

            // Отримати квитки за їхніми ObjectId
            List<Document> tickets = ticketsCollection.find(in("_id", ticketIds)).into(new ArrayList<>());
            List<Ticket> ticketObjects = new ArrayList<>();
            for (Document ticket : tickets) {
                byte[] image = ticket.get("image", Binary.class).getData();
                ticketObjects.add(new Ticket(ticket.getObjectId("_id"), ticket.getString("ticket_type"),
                        ticket.getString("ticket_name"), ticket.getString("description"), ticket.getDouble("price"), image,
                        ticket.getList("duration_options", String.class), ticket.getList("food_options", String.class),
                        ticket.getList("transport_options", String.class)));
            }
            return ticketObjects;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int addTicketToUser(ObjectId ticketId, String duration, String food, String transport) {
        try  {
            Document user = usersCollection.find(and(eq("_id", User.getUserId()), eq("tickets._id", ticketId))).first();
            if (user != null) {
                // Квиток вже існує, не додаємо його знову
                return 1; // Повертаємо 2, якщо квиток вже існує
            }
            Document newTicket = new Document("_id", ticketId)
                    .append("duration", duration)
                    .append("food", food)
                    .append("transport", transport);
            usersCollection.updateOne(eq("_id", User.getUserId()), push("tickets", newTicket));
            User.addUserTicket(new UserTicket(ticketId, duration, food, transport));
            return 0; // Повертаємо 0, якщо оновлення було успішним
        } catch (Exception e) {
            e.printStackTrace();
            return 2; // Повертаємо 1, якщо сталася помилка
        }
    }
    public static int deleteTicketFromUser(ObjectId ticketId) {
        try {
            Document query = new Document("_id", User.getUserId());
            Document update = new Document("$pull", new Document("tickets", new Document("_id", ticketId)));

            // Виконання запиту оновлення
            UpdateResult result = usersCollection.updateOne(query, update);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // Повертаємо 1, якщо сталася помилка
        }
    }
}
