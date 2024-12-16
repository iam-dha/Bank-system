package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.Credential;
import model.Transaction;
import model.User;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private VBox manage;
    @FXML
    private VBox transfer;
    @FXML
    private VBox history;
    // Manage element
    @FXML
    private VBox activeUser;
    @FXML
    private VBox userInformation;
    @FXML
    private TableView<User> activeUserTable;
    @FXML
    private TableColumn<User, String> accountColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, String> balanceColumn;
    @FXML
    private Label searchErrorLabel;
    @FXML
    private TextField accountToSearch;
    @FXML
    private Label historyError;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;


    private String _fromdate = "01/01/2024";
    private String _todate = "10/12/2024";
    private String _account;
    private String _token;
    private Credential credetial;
    private ArrayList<User> activeUserList;

    public void initialize(URL url, ResourceBundle rb) {
        transfer.setVisible(false);
        transfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        manage.setVisible(true);
        manage.setMouseTransparent(false);
        fromDate.setOnAction(e -> {
            LocalDate selectedDate = fromDate.getValue(); // Get selected date
            if (selectedDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                _fromdate = selectedDate.format(formatter);
            } else {
                System.out.println("No date selected.");
            }
        });
        toDate.setOnAction(e -> {
            LocalDate selectedDate = toDate.getValue(); // Get selected date
            if (selectedDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                _todate = selectedDate.format(formatter);
            } else {
                System.out.println("No date selected.");
            }
        });
        credetial = User.getCredential();
        _token = credetial.getToken();
        switchToManage();
    }

    public void switchToManage(){
        transfer.setVisible(false);
        transfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        manage.setVisible(true);
        manage.setMouseTransparent(false);
        activeUser.setVisible(true);
        activeUser.setMouseTransparent(false);
        userInformation.setVisible(false);
        userInformation.setMouseTransparent(true);
        searchErrorLabel.setVisible(false);
        setActiveUser();
    }

    public void switchToTranfer(){

    }

    public void switchToHistory(){
        historyError.setVisible(false);
        transfer.setVisible(false);
        transfer.setMouseTransparent(true);
        manage.setVisible(false);
        manage.setMouseTransparent(true);
        history.setVisible(true);
        history.setMouseTransparent(false);
        fromDate.setValue(LocalDate.of(2024, 1, 1));
        toDate.setValue(LocalDate.now());
    }

    public void setActiveUser(){
        activeUserList = new ArrayList<>();
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/admin/get-all-active-session"))
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                activeUserList = objectMapper.readValue(response.body(), new TypeReference<ArrayList<User>>() {});
                ObservableList<User> dataList = FXCollections.observableArrayList(activeUserList);
                accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
                firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
                lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
                emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
                phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
                balanceColumn.setCellValueFactory(new PropertyValueFactory<>("fund"));

                activeUserTable.setItems(dataList);
            }
            catch (Exception except) {
                except.printStackTrace();
            }
        }
    }

    public void getUserInformation(){
        String _accountToSearch = accountToSearch.getText();
        if (_accountToSearch == null|| _accountToSearch.trim().isEmpty()){
            searchErrorLabel.setText("ERROR : Type the account to Search");
            searchErrorLabel.setVisible(true);
        }
        else {
            HttpResponse<String> response = null;
            try {
                String baseUrl = "http://13.239.134.221:8080/api/v1/user/information";
                String endpoint = String.format("%s?account=%s", baseUrl, _accountToSearch);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(endpoint))
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("Bearer %s", _token))
                        .GET()
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                searchErrorLabel.setText("ERROR : Server Error.");
                searchErrorLabel.setVisible(true);
            }
            else {
                int reponse_status = response.statusCode();
                if (reponse_status == 200) {
                    activeUser.setVisible(false);
                    activeUser.setMouseTransparent(true);
                    userInformation.setVisible(true);
                    userInformation.setMouseTransparent(false);
                }
                else {
                    searchErrorLabel.setText("ERROR : Invalid Account");
                    searchErrorLabel.setVisible(true);
                }
            }

        }
    }

    public void getHistoryTransaction(){

    }

    public void changeUserInformation(){

    }

    public void transferFund(){

    }

    public void removeUser(){

    }

    @FXML
    public void logOut(ActionEvent event){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation"); // Changed to English
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?"); // Changed to English

            // Change the message text color
            alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: #576aca;");

            // Change the OK button color
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton, ButtonType.CANCEL);
            alert.getDialogPane().lookupButton(okButton).setStyle("-fx-background-color: #576aca; -fx-text-fill: white;");

            alert.showAndWait().ifPresent(response -> {
                if (response == okButton) {
                    // Perform logout here
                    SceneController controller = new SceneController();
                    controller.switchToLogin(event);
                }
            });
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
