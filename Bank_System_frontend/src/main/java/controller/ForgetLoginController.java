package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class ForgetLoginController implements Initializable {
    @FXML
    private Label error;
    @FXML
    private Label notifi;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private CheckBox checkBox;
    @FXML
    private VBox VerifyScene;
    @FXML
    private VBox registerScene;

    private String _account;
    private String _email;

    public void initialize(URL url, ResourceBundle rb) {
        error.setVisible(false);
        notifi.setVisible(false);
    }

    public void switchLoginScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToLogin(event);
    }

    public void sumbit(ActionEvent event){
        //check = false;
        if(checkBox.isSelected()){
            notifi.setVisible(true);
            userTextField.getStyleClass().remove("error");
            emailTextField.getStyleClass().remove("error");
            _account = userTextField.getText();
            _email = emailTextField.getText();
            HttpResponse<String> response = null ;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://3.27.209.207:8080/api/v1/auth/register"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"account\" : \"%s\",\"email\": \"%s\"}", _account, _email)
                        ))
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }
            catch (Exception e){
                System.out.println("Cannot");
            }
            if(response.statusCode() == 401) {
                //Have account associated with email and username
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://3.27.209.207:8080/api/v1/auth/forget-password"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(
                                    String.format("{\"account\" : \"%s\"}", _account)
                            ))
                            .build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                }
                catch (Exception e){
                    error.setText("Something went wrong. Try again later!");
                    error.setVisible(true);
                }
                if (response.statusCode() == 200) {
                    VerifyScene.setVisible(true);
                    registerScene.setVisible(false);
                    registerScene.setMouseTransparent(true);
                    VerifyScene.setMouseTransparent(false);
                }
            }
            else {
                error.setText("We couldn't find an account associated with this account. Try again");
                error.setVisible(true);
            }
        }
        else {
            error.setText("Make sure that you agree to the terms.");
            error.setVisible(true);
            userTextField.getStyleClass().add("error");
            emailTextField.getStyleClass().add("error");
        }
    }
}
