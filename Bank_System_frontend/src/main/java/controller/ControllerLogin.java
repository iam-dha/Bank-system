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
    }


    private User s;
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
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToMainScene(event);
        if (user_name.trim().isEmpty() || password.isEmpty()) {
            notification.setText("Please fill in the blank.");
            notification.setVisible(true);
        }
        else {
            System.out.println(password);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://jsonplaceholder.typicode.com/posts"))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"account\":\"%s\", \"password\":\"%s\"}", user_name, password)
                        ))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    ObjectMapper mapper = new ObjectMapper();

                }
                ObjectMapper mapper = new ObjectMapper();

                // Deserialize the JSON response to a list of Users
                List<User> users = Arrays.asList(mapper.readValue(response.body(), User[].class));

                // Access a specific user
                User user = new User();
                user.setAccount("DCM");
                s = user;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public User getS() {
        return s;
    }

    public void setS(User s) {
        this.s = s;
    }

}
