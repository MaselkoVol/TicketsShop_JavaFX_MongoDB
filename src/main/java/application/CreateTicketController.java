package application;

import application.src.User;
import application.src.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CreateTicketController {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private File imageFile;
    @FXML
    private ChoiceBox ticketTypes;
    @FXML
    private TextField ticketName;
    @FXML
    private TextArea description;
    @FXML
    private TextField price;
    @FXML
    private TextField durationField;
    @FXML
    private ChoiceBox durationOptions;
    @FXML
    private TextField foodField;
    @FXML
    private ChoiceBox foodOptions;
    @FXML
    private TextField transportField;
    @FXML
    private ChoiceBox transportOptions;
    @FXML
    private TextField imageField;
    @FXML
    private Label messageLabel;
    @FXML
    public void initialize() {
        List<String> ticketTypesList = DBManager.getTicketTypes();
        ticketTypes.setItems(FXCollections.observableArrayList(ticketTypesList));
    }


    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
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


    @FXML
    public void addDurationOption () {
        if (durationField.getText().isEmpty()) {
            return;
        }
        durationOptions.getItems().add(durationField.getText());
        durationField.setText("");
    }
    @FXML
    public void deleteDurationOption () {
        if (durationOptions.getItems().isEmpty() || durationOptions.getValue() == null) {
            return;
        }
        durationOptions.getItems().remove( durationOptions.getValue());
        if (durationOptions.getItems().isEmpty()) {
            return;
        }
        durationOptions.setValue(durationOptions.getItems().get(0));
    }
    @FXML
    public void addFoodOption () {
        if (foodField.getText().isEmpty()) {
            return;
        }
        foodOptions.getItems().add(foodField.getText());
        foodField.setText("");
    }
    @FXML
    public void deleteFoodOption () {
        if (foodOptions.getItems().isEmpty() || foodOptions.getValue() == null) {
            return;
        }
        foodOptions.getItems().remove( foodOptions.getValue());
        if (foodOptions.getItems().isEmpty()) {
            return;
        }
        foodOptions.setValue(foodOptions.getItems().get(0));
    }
    @FXML
    public void addTransportOption () {
        if (transportField.getText().isEmpty()) {
            return;
        }
        transportOptions.getItems().add(transportField.getText());
        transportField.setText("");
    }
    @FXML
    public void deleteTransportOption () {
        if (transportOptions.getItems().isEmpty() || transportOptions.getValue() == null) {
            return;
        }
        transportOptions.getItems().remove( transportOptions.getValue());
        if (transportOptions.getItems().isEmpty()) {
            return;
        }
        transportOptions.setValue(transportOptions.getItems().get(0));
    }


    @FXML
    private void chooseImage () {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
        imageFile = fileChooser.showOpenDialog(null);

        if (imageFile == null) {
            imageField.setText("");
            return;
        }

        String imagePath = imageFile.getPath();
        int maxLength = 19; // Максимальна довжина відображення
        if (imagePath.length() > maxLength) {
            imageField.setText("..." + imagePath.substring(imagePath.length() - maxLength));
        } else {
            imageField.setText(imagePath);
        }
    }


    @FXML
    private void handleSubmitButtonAction(ActionEvent event) throws IOException {
        String ticketNameText = ticketName.getText();
        String descriptionText = description.getText();
        String priceText = price.getText();

        messageLabel.setTextFill(Color.RED);
        if (ticketTypes.getValue() == null || ticketNameText.isEmpty() || descriptionText.isEmpty()
                || priceText.isEmpty() || imageFile == null || durationOptions.getItems().isEmpty()) {
            messageLabel.setText("Всі необхідні поля повинні бути заповнені!");
            return;
        }
        if (!Utils.isValidPrice(priceText)) {
            messageLabel.setText("Некоректна ціна. Введіть циле число, або з двома цифрами після крапки!");
            return;
        }

        String ticketTypeText = (String) ticketTypes.getValue();
        double priceDouble = Double.parseDouble(priceText);
        byte [] byteImage = Files.readAllBytes(imageFile.toPath());
        List<String> durationItems = new ArrayList<>(durationOptions.getItems());
        List<String> foodItems = new ArrayList<>(foodOptions.getItems());
        if (foodItems.isEmpty()) {
            foodItems.add("Немає");
        }
        List<String> transportItems = new ArrayList<>(transportOptions.getItems());
        if (transportItems.isEmpty()) {
            transportItems.add("Немає");
        }

        int result = DBManager.createTicket(ticketTypeText, ticketNameText, descriptionText, priceDouble,
                                            byteImage, durationItems, foodItems, transportItems);
        if (result == 0) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Путівка успішно додана!");
            return;
        }
        if (result == 1) {
            messageLabel.setText("Щось пішло не так, спробуйте знову.");
        }
        messageLabel.setText("");

    }
}
