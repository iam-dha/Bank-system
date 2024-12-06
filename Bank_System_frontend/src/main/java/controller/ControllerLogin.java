package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordText;
    @FXML
    private ToggleButton hide;
    @FXML
    private ImageView eye;
    @FXML
    private ImageView hidden_eye;
    @FXML
    private Label notification;
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        notification.setVisible(false);
    }

    public void togglePasswordVisibility() {
        //isPasswordVisible = !isPasswordVisible;
        if (hide.isSelected()) {
            passwordText.setText(passwordField.getText());
            eye.setVisible(false);
            hidden_eye.setVisible(true);
            passwordText.setVisible(true);
            passwordField.setVisible(false);

        } else {
            passwordField.setText(passwordText.getText());
            eye.setVisible(true);
            hidden_eye.setVisible(false);
            passwordField.setVisible(true);
            passwordText.setVisible(false);
        }
    }

    public void switchForgetLoginScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToForgetLogin(event);
    }
    public void switchCreateAccountScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToCreateAccount(event);
    }
    public void switchMaineScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToMainScene(event);
    }

    public void signIn(ActionEvent event) {
        String user_name = username.getText();
        String password = passwordText.getText();
        if (user_name.trim().isEmpty() || password.isEmpty()) {
            notification.setText("Please fill in the blank.");
            notification.setVisible(true);
        }
        else {
            System.out.println(password);
//            try {
//                HttpClient client = HttpClient.newHttpClient();
//
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(new URI(''))
//            }
        }


    }

}
