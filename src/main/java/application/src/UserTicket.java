package application.src;

import org.bson.types.ObjectId;

public class UserTicket {
    private ObjectId ticketId;
    private String duration;
    private String food;
    private String transport;

    public UserTicket (ObjectId ticketId, String duration, String food,String transport ) {
        this.ticketId = ticketId;
        this.duration = duration;
        this.food = food;
        this.transport = transport;
    }

    public ObjectId getTicketId() {
        return ticketId;
    }

    public String getDuration() {
        return duration;
    }

    public String getFood() {
        return food;
    }

    public String getTransport() {
        return transport;
    }
}
