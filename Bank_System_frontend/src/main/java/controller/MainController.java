package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Credential;
import model.User;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox tranfer;
    @FXML
    private VBox history;
    @FXML
    private HBox overview;
    private User user;
    private Credential credetial;
    public void initialize(URL url, ResourceBundle rb) {
        getUserInformation();
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
    }
    public void switchToTranfer(){
        getUserInformation();
        tranfer.setVisible(true);
        tranfer.setMouseTransparent(false);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
    }
    public void switchToOverview(){
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
        history.setVisible(false);
        history.setMouseTransparent(true);
    }
    public void switchToHistory(){
        getUserInformation();
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(true);
        history.setMouseTransparent(false);
    }
    public void getUserInformation(){
        Credential credential = User.getCredential();
        String _token = credential.getToken();
        String _account = credential.getAccount();
        try {
            String baseUrl = "http://3.27.209.207:8080/api/v1/user/information";
            String endpoint = String.format("%s?account=%s", baseUrl, _account);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", _token))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                user = objectMapper.readValue(responseBody, User.class);
                System.out.println(user.getEmail());
            }
            else if (response.statusCode() == 403) {
                System.out.println("Back to login");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



}
