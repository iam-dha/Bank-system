package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        togglePasswordVisibility();
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
            passwordText.setVisible(false);
            passwordField.setVisible(true);
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
        String password = passwordField.getText();
        System.out.println(user_name + password);
        if (user_name.trim().isEmpty() || password.isEmpty()) {
            notification.setText("Please fill in the blank.");
            notification.setVisible(true);
        }
        else {
            System.out.println(password);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://3.27.209.207:8080/api/v1/auth/authenticate"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"account\":\"%s\", \"password\":\"%s\"}", user_name, password)
                        ))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int statusCode = response.statusCode();
                if (statusCode == 200){
                    String responseBody = response.body();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Credential credential = objectMapper.readValue(responseBody, Credential.class);
                    System.out.println(responseBody);
                    User.setCredential(credential);
                    SceneController sceneCotroller = new SceneController();
                    sceneCotroller.switchToMainScene(event);
                }
                else if (statusCode == 401) {
                    notification.setText("Invalid Account!");
                    notification.setVisible(true);
                }
                else if (statusCode == 403) {
                    notification.setText("Wrong password. Please try again.!");
                    notification.setVisible(true);
                }
                else {
                    notification.setText("Something went wrong. Try again.!");
                    notification.setVisible(true);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
