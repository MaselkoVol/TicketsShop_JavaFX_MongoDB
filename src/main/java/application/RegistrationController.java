package application;

import application.src.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField repeatPassword;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Label messageLabel;
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleSubmitButtonAction(ActionEvent event) throws IOException {
        String userNameText = userName.getText();
        String passwordText = password.getText();
        String repeatPasswordText = repeatPassword.getText();
        String phoneNumberText = phoneNumber.getText();
        messageLabel.setTextFill(Color.RED);
        if (userNameText.isEmpty() || passwordText.isEmpty() || repeatPasswordText.isEmpty() || phoneNumberText.isEmpty()) {
            messageLabel.setText("Всі поля повинні бути заповнені!");
            return;
        }
        if (!passwordText.equals(repeatPasswordText)) {
            messageLabel.setText("Паролі не співпадають!");
            return;
        }
        if (passwordText.length() < 6) {
            messageLabel.setText("Пароль повинен бути не меншим ніж 6 символів!");
            return;
        }
        if (!Utils.validatePhoneNumber(phoneNumberText)) {
            messageLabel.setText("Неправильний номер телефону! +380984465191 (приклад)");
            return;
        }
        int result = DBManager.createUser(userNameText, passwordText,phoneNumberText);
        if (result == 0) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Ви успішно зареєструвались!");
            switchToLogin(event);
            return;
        }
        // user is already registered
        if (result == 1) {
            messageLabel.setText("Користувач з таким іменем уже існує!");
            return;
        }
        if (result == 2) {
            messageLabel.setText("Щось пішло не так, спробуйте знову.");
            return;
        }

    }
}