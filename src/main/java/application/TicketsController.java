package application;

import application.src.Ticket;
import application.src.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TicketsController {
    @FXML
    private ChoiceBox ticketTypes;
    @FXML
    private VBox container;
    @FXML
    private VBox pagination;
    @FXML ScrollPane scrollContainer;
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private void renderTickets () {
        List<Ticket> tickets = DBManager.getAllTickets((String) ticketTypes.getValue());
        pagination.getChildren().removeAll(pagination.getChildren());
        for (Ticket ticket: tickets) {
            pagination.getChildren().add(ticket.createTicketBox());
        }
        pagination.requestLayout();
        scrollContainer.layout();
    }
    @FXML
    public void initialize() {
        List<String> ticketTypesList = DBManager.getTicketTypes();
        ticketTypes.setItems(FXCollections.observableArrayList(ticketTypesList));
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
    public void switchToUserTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("UserTickets.fxml"));
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
