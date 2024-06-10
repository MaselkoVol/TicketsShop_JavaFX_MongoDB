package application.src;

import org.bson.types.ObjectId;

import java.util.List;

public class User {
    private static ObjectId userId = null;
    private static String userName = null;
    private static String password = null;
    private static String phoneNumber = null;
    private static List<UserTicket> tickets = null;
    public static void setUser(ObjectId userId, String userName, String password, String phoneNumber, List<UserTicket> tickets) {
        User.userId = userId;
        User.userName = userName;
        User.password = password;
        User.phoneNumber = phoneNumber;
        User.tickets = tickets;
    }
    public static void resetUser () {
        User.userId = null;
        User.userName = null;
        User.password = null;
        User.phoneNumber = null;
        User.tickets = null;
    }
    public static List<UserTicket> getTickets () {
        return tickets;
    }
    public static void addUserTicket(UserTicket ticket) {
        tickets.add(ticket);
    }
    public static void removeUserTicket(UserTicket ticket) {
        tickets.remove(ticket);
    }
    public static ObjectId getUserId () {
        return User.userId;
    }
    private User () {}
}