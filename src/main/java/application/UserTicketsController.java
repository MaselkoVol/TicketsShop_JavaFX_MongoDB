package application;

import application.src.Ticket;
import application.src.User;
import application.src.UserTicket;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

public class UserTicketsController {
    @FXML
    private VBox container;
    @FXML
    private VBox pagination;
    @FXML
    ScrollPane scrollContainer;
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private void renderTickets () {
        List<Ticket> tickets = DBManager.getAllUserTickets();
        pagination.getChildren().removeAll(pagination.getChildren());
        List<UserTicket> userTickets = User.getTickets();
        for (Ticket ticket: tickets) {
            for (UserTicket userTicket: userTickets) {
                if (ticket.getTicketId().equals(userTicket.getTicketId())) {
                    VBox curUserTicket = ticket.createUserTicketBox(userTicket.getDuration(), userTicket.getFood(), userTicket.getTransport());
                    System.out.println(curUserTicket.getChildren());
                    Button deleteButton = (Button) curUserTicket.lookup("#deleteButton");
                    Label messageLabel = (Label) curUserTicket.lookup("#messageLabel");
                    deleteButton.setOnAction(event -> {
                        int result = DBManager.deleteTicketFromUser(userTicket.getTicketId());
                        messageLabel.setTextFill(Color.RED);
                        if (result == 0) {
                            messageLabel.setTextFill(Color.GREEN);
                            messageLabel.setText("Ви успішно видалили путівку!");
                            User.removeUserTicket(userTicket);
                            renderTickets();
                            return;
                        }
                        if (result == 1) {
                            messageLabel.setText("Щось пішло не так, спробуйте знову.");
                            return;
                        }
                    });
                    pagination.getChildren().add(curUserTicket);
                    break;
                }
            }
        }
        pagination.requestLayout();
        scrollContainer.layout();
    }
    @FXML
    public void initialize() {
        renderTickets();
    }
    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToCreateTicket(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CreateTicket.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Tickets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void handleExitButton (ActionEvent event) throws IOException {
        User.resetUser();
        switchToLogin(event);
    }
}
