package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    @FXML
    private StackPane passwordPane;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordText;
    @FXML
    private Button signInButton;
    @FXML
    private ToggleButton hide;
    @FXML
    private ImageView eye;
    @FXML
    private ImageView hidden_eye;
    @FXML
    private Label invalid;
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        invalid.setVisible(false);
    }

    public void togglePasswordVisibility(ActionEvent event) {
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
    public void login(ActionEvent event) {
        String name = username.getText();
        String pass = passwordText.getText();
        System.out.println(name);
        System.out.println(pass);
        invalid.setVisible(false); // backend check from data base if it is valid
        
    }

    public void switchForgetLoginScene(ActionEvent event) throws IOException {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToForgetLogin(event);
    }

}
