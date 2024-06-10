package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import application.SceneController;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label messageLabel;
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private void switchToRegistration(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Tickets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) throws IOException {
        String userNameText = userName.getText();
        String passwordText = password.getText();
        messageLabel.setTextFill(Color.RED);
        if (userNameText.isEmpty() || passwordText.isEmpty()) {
            messageLabel.setText("Всі поля повинні бути заповнені!");
            return;
        }
        int result = DBManager.loginUser(userNameText, passwordText);
        if (result == 0) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Ви успішно увійшли!");
            switchToTickets(event);
            return;
        }
        // user is already registered
        if (result == 1) {
            messageLabel.setText("Користувача з таким іменем не знайдено!");
            return;
        }
        if (result == 2) {
            messageLabel.setText("Неправильний пароль!");
            return;
        }
        if (result == 2) {
            messageLabel.setText("Щось пішло не так, спробуйте знову.");
            return;
        }

    }
}