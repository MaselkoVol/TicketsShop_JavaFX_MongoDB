package application.src;

import application.DBManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.bson.types.ObjectId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class Ticket {
    private ObjectId ticketId;
    private String ticketType;
    private String ticketName;
    private String description;
    private double price;
    private byte[] image;
    private List<String> durationOptions;
    private List<String> foodOptions;
    private List<String> transportOptions;


    public Ticket (ObjectId ticketId, String ticketType, String ticketName, String description, double price,
                   byte[] image, List<String> durationOptions, List<String> foodOptions, List<String> transportOptions) {
        setTicket(ticketId, ticketType, ticketName, description, price, image, durationOptions, foodOptions, transportOptions);
    }
    public void setTicket (ObjectId ticketId, String ticketType, String ticketName, String description, double price,
                           byte[] image, List<String> durationOptions, List<String> foodOptions, List<String> transportOptions) {
        this.ticketId = ticketId;
        this.ticketType = ticketType;
        this.ticketName = ticketName;
        this.description = description;
        this.price = price;
        this.image = image;
        this.durationOptions = durationOptions;
        this.foodOptions = foodOptions;
        this.transportOptions = transportOptions;
    }
    public ObjectId getTicketId () {
        return ticketId;
    }

    public VBox createUserTicketBox(String duration, String food, String transport ) {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: black; -fx-padding: 10; -fx-spacing: 10;");
        VBox.setMargin(vbox, new Insets(0, 0, 20, 0));
        HBox header = new HBox();

        Label ticketNameLabel = new Label(ticketName);
        ticketNameLabel.setFont(Font.font(16)); // Set font size

        Label typeNameLabel = new Label("Тип: " + ticketType);
        typeNameLabel.setFont(Font.font(16)); // Set font size

        Region spacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(spacer, Priority.ALWAYS);

        GridPane.setHalignment(typeNameLabel, HPos.RIGHT);

        header.getChildren().addAll(ticketNameLabel, spacer, typeNameLabel);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-style: italic; -fx-font-size: 16;"); // Italicize the description and set font size

        Label priceLabel = new Label("Ціна: ₴" + price);
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 16;");

        Pane imageContainer = new Pane();

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: black;");
        imageView.fitWidthProperty().bind(imageContainer.widthProperty());

        InputStream inputStream = new ByteArrayInputStream(image);
        Image finalImage = new Image(inputStream);
        imageView.setImage(finalImage);

        imageContainer.getChildren().add(imageView);

        HBox optionsLabel = new HBox();
        optionsLabel.setSpacing(10);
        optionsLabel.setPadding(new Insets(0, 0, -10, 0));

        Label durationLabel = new Label("Тривалість");
        durationLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        Region durationLabelSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(durationLabelSpacer, Priority.ALWAYS);

        Label foodLabel = new Label("Харчування");
        foodLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        Region foodLabelSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(foodLabelSpacer, Priority.ALWAYS);

        Label transportLabel = new Label("Транспорт");
        transportLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        optionsLabel.getChildren().addAll(durationLabel, durationLabelSpacer, foodLabel, foodLabelSpacer, transportLabel);

        HBox options = new HBox();
        optionsLabel.setSpacing(10);
        optionsLabel.setPadding(new Insets(0, 0, -10, 0));

        Label durationElement = new Label(duration);
        durationElement.setStyle("-fx-font-weight: bold;");
        durationElement.minWidthProperty().bind(options.widthProperty().divide(3.4));

        Region durationElementSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(durationElementSpacer, Priority.ALWAYS);

        Label foodElement = new Label(food);
        foodElement.setStyle("-fx-font-weight: bold;");
        foodElement.minWidthProperty().bind(options.widthProperty().divide(3.4));

        Region foodElementSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(foodElementSpacer, Priority.ALWAYS);

        Label transportElement = new Label(transport);
        transportElement.setStyle("-fx-font-weight: bold;");
        transportElement.minWidthProperty().bind(options.widthProperty().divide(3.4));

        options.getChildren().addAll(durationElement, durationElementSpacer, foodElement, foodElementSpacer, transportElement);

        Label messageLabel = new Label("");
        messageLabel.setId("messageLabel");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setPadding(new Insets(-10, 0, -10, 0));

        Button deleteButton = new Button("Видалити путівку");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setId("deleteButton");
        vbox.getChildren().addAll(header, descriptionLabel, imageContainer, priceLabel, optionsLabel, options, messageLabel, deleteButton);
        return vbox;
    }

    public VBox createTicketBox() {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #CCCCCC; -fx-border-color: black; -fx-padding: 10; -fx-spacing: 10;");
        VBox.setMargin(vbox, new Insets(0, 0, 20, 0));
        HBox header = new HBox();

        Label ticketNameLabel = new Label(ticketName);
        ticketNameLabel.setFont(Font.font(16)); // Set font size

        Label typeNameLabel = new Label("Тип: " + ticketType);
        typeNameLabel.setFont(Font.font(16)); // Set font size

        Region spacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(spacer, Priority.ALWAYS);

        GridPane.setHalignment(typeNameLabel, HPos.RIGHT);

        header.getChildren().addAll(ticketNameLabel, spacer, typeNameLabel);

        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-style: italic; -fx-font-size: 16;"); // Italicize the description and set font size

        Label priceLabel = new Label("Ціна: ₴" + price);
        priceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 16;");

        Pane imageContainer = new Pane();

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: black;");
        imageView.fitWidthProperty().bind(imageContainer.widthProperty());

        InputStream inputStream = new ByteArrayInputStream(image);
        Image finalImage = new Image(inputStream);
        imageView.setImage(finalImage);

        imageContainer.getChildren().add(imageView);

        HBox optionsLabel = new HBox();
        optionsLabel.setSpacing(10);
        optionsLabel.setPadding(new Insets(0, 0, -10, 0));

        Label durationLabel = new Label("Тривалість");
        durationLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        Region durationLabelSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(durationLabelSpacer, Priority.ALWAYS);

        Label foodLabel = new Label("Харчування");
        foodLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        Region foodLabelSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(foodLabelSpacer, Priority.ALWAYS);

        Label transportLabel = new Label("Транспорт");
        transportLabel.minWidthProperty().bind(optionsLabel.widthProperty().divide(3.4));

        optionsLabel.getChildren().addAll(durationLabel, durationLabelSpacer, foodLabel, foodLabelSpacer, transportLabel);

        HBox options = new HBox();

        ChoiceBox<String> durationElement = new ChoiceBox<>();
        durationElement.getItems().addAll(durationOptions);
        durationElement.setValue(durationOptions.get(0));
        durationElement.minWidthProperty().bind(options.widthProperty().divide(3.4));

        Region durationElementSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(durationElementSpacer, Priority.ALWAYS);

        ChoiceBox<String> foodElement = new ChoiceBox<>();
        foodElement.getItems().addAll(foodOptions);
        foodElement.setValue(foodOptions.get(0));
        foodElement.minWidthProperty().bind(options.widthProperty().divide(3.4));

        Region foodElementSpacer = new Region(); // Створюємо пустий розширювач
        HBox.setHgrow(foodElementSpacer, Priority.ALWAYS);

        ChoiceBox<String> transportElement = new ChoiceBox<>();
        transportElement.getItems().addAll(transportOptions);
        transportElement.setValue(transportOptions.get(0));
        transportElement.minWidthProperty().bind(options.widthProperty().divide(3.4));


        durationElement.setMaxWidth(Double.MAX_VALUE);
        foodElement.setMaxWidth(Double.MAX_VALUE);
        transportElement.setMaxWidth(Double.MAX_VALUE);

        options.getChildren().addAll(durationElement, durationElementSpacer, foodElement, foodElementSpacer, transportElement);

        Label messageLabel = new Label("");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setPadding(new Insets(-10, 0, -10, 0));

        Button addButton = new Button("Додати в мої путівки");
        addButton.setMaxWidth(Double.MAX_VALUE);

        addButton.setOnAction(event -> {
            String selectedDuration = durationElement.getValue();
            String selectedFood = foodElement.getValue();
            String selectedTransport = transportElement.getValue();

            int result = DBManager.addTicketToUser(ticketId, selectedDuration, selectedFood, selectedTransport);
            messageLabel.setTextFill(Color.RED);
            if (result == 0) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("Успішно додано!");
                return;
            }
            if (result == 1) {
                messageLabel.setText("Ви вже взяли цю путівку!");
                return;
            }
            if (result == 2) {
                messageLabel.setText("Щось пішло не так, спробуйте знову.");
                return;
            }
        });

        vbox.getChildren().addAll(header, descriptionLabel, imageContainer, priceLabel, optionsLabel, options, messageLabel, addButton);

        return vbox;
    }
}
