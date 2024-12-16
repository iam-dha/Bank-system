package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
import java.util.Collections;
import java.util.Comparator;
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
    private TextField fnameTextField;
    @FXML
    private TextField lnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField addrTextField;
    @FXML
    private TextField fundTextField;
    @FXML
    private PasswordField newpassword;
    @FXML
    private VBox userIdentity;
    @FXML
    private HBox editField;
    @FXML
    private HBox submitField;
    //Transfer scene
    @FXML
    private TextField decripstionTextField;
    @FXML
    private TextField amountTextField;
    @FXML
    private ChoiceBox myChoiceBox;
    @FXML
    private TextField receiverAccTextfield;
    @FXML
    private Label transactionErrLabel;
    @FXML
    private TextField senderTextfield;
    @FXML
    private Button send;
    @FXML
    private TextField amountDeposit;
    @FXML
    private TextField depositReceiver;
    @FXML
    private Label depositErrLabel;
    // History scene
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> timeColumn;
    @FXML
    private TableColumn<Transaction, String> fromAccountColumn;
    @FXML
    private TableColumn<Transaction, String> toAccountColumn;
    @FXML
    private TableColumn<Transaction, String> messageColumn;
    @FXML
    private TextField keywordToSearch;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label historyError;
    @FXML
    private TextField accountToSearch;


    private String _fromdate = "01/01/2024";
    private String _todate = "31/12/2024";
    private String _account;
    private String _token;
    private Credential credetial;
    private ArrayList<User> activeUserList;
    private User user;

    public void initialize(URL url, ResourceBundle rb) {
        transfer.setVisible(false);
        transfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        manage.setVisible(true);
        manage.setMouseTransparent(false);
        myChoiceBox.getItems().addAll("HUST BANK", "NEU BANK", "HNUE BANK");
        myChoiceBox.setValue("HUST BANK");
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
        accountToSearch.clear();
        searchErrorLabel.setTextFill(Color.RED);
        setActiveUser();
    }

    public void switchToTranfer(ActionEvent e){
        cleartransfer();
        clearDeposit();
        depositErrLabel.setVisible(false);
        transactionErrLabel.setVisible(false);
        manage.setVisible(false);
        manage.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        transfer.setVisible(true);
        transfer.setMouseTransparent(false);
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
        keywordToSearch.clear();
        transactionTable.getItems().clear();
    }

    private void setUserInformation(){
        fnameTextField.setText(user.getFirstname());
        lnameTextField.setText(user.getLastname());
        emailTextField.setText(user.getEmail());
        userTextField.setText(user.getAccount());
        phoneTextField.setText(user.getPhonenumber());
        addrTextField.setText(user.getAddress());
        fundTextField.setText(user.getFund());
    }

    public void setActiveUser(){
        activeUserList = new ArrayList<>();
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/admin/get-all-active-session"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", _token))
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            if (response.statusCode() == 200) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    activeUserList = objectMapper.readValue(response.body(), new TypeReference<ArrayList<User>>() {
                    });
                    ObservableList<User> dataList = FXCollections.observableArrayList(activeUserList);
                    accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));
                    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
                    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
                    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
                    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
                    balanceColumn.setCellValueFactory(new PropertyValueFactory<>("fund"));
                    activeUserTable.setItems(dataList);
                } catch (Exception except) {
                    except.printStackTrace();
                }
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
                    searchErrorLabel.setVisible(false);
                    activeUser.setVisible(false);
                    activeUser.setMouseTransparent(true);
                    userInformation.setVisible(true);
                    userInformation.setMouseTransparent(false);
                    userIdentity.setMouseTransparent(true);
                    editField.setVisible(true);
                    editField.setMouseTransparent(false);
                    submitField.setVisible(false);
                    submitField.setMouseTransparent(true);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        user = objectMapper.readValue(response.body(), User.class);
                        setUserInformation();
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                    }

                }
                else {
                    searchErrorLabel.setText("ERROR : Invalid Account");
                    searchErrorLabel.setVisible(true);
                }
            }

        }
    }


    public void changeUserInformation(){
        userIdentity.setMouseTransparent(false);
        userTextField.setMouseTransparent(true);
        fundTextField.setMouseTransparent(true);
        editField.setVisible(false);
        editField.setMouseTransparent(true);
        submitField.setVisible(true);
        submitField.setMouseTransparent(false);
    }

    public void cancelChange(){
        activeUser.setVisible(false);
        activeUser.setMouseTransparent(true);
        userInformation.setVisible(true);
        userInformation.setMouseTransparent(false);
        userIdentity.setMouseTransparent(true);
        editField.setVisible(true);
        editField.setMouseTransparent(false);
        submitField.setVisible(false);
        submitField.setMouseTransparent(true);
        setUserInformation();
    }

    public void submitChange(){
        searchErrorLabel.setVisible(false);
        HttpResponse<String> response = null;
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/admin/change-user-information"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", _token))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"account\":\"%s\"," +
                                            "\"password\":\"%s\"," +
                                            "\"firstName\":\"%s\"," +
                                            "\"lastName\":\"%s\"," +
                                            "\"email\":\"%s\"," +
                                            "\"address\":\"%s\"," +
                                            "\"phoneNumber\":\"%s\"}", userTextField.getText(), newpassword.getText(), fnameTextField.getText(),
                                    lnameTextField.getText(), emailTextField.getText(), addrTextField.getText(), phoneTextField.getText())
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 403){
                searchErrorLabel.setText("Email is Linked with another account");
                searchErrorLabel.setTextFill(Color.RED);
                searchErrorLabel.setVisible(true);
                emailTextField.setText(user.getEmail());
                userIdentity.setMouseTransparent(false);
            }
            else if (response.statusCode() == 200){
                searchErrorLabel.setText("Account change successfully");
                searchErrorLabel.setTextFill(Color.GREEN);
                searchErrorLabel.setVisible(true);
                userInformation.setVisible(true);
                userInformation.setMouseTransparent(false);
                userIdentity.setMouseTransparent(true);
                editField.setVisible(true);
                editField.setMouseTransparent(false);
                submitField.setVisible(false);
                submitField.setMouseTransparent(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeUser(){
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/admin/delete-user"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", _token))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"account\":\"%s\"}", user.getAccount())
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(user.getAccount());
            System.out.println(response.statusCode());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        switchToManage();
        searchErrorLabel.setText("Delete user successfully");
        searchErrorLabel.setTextFill(Color.GREEN);
        searchErrorLabel.setVisible(true);
    }


    @FXML
    public void transferFund(ActionEvent event){
        String _fromaccount = senderTextfield.getText();
        String _message = decripstionTextField.getText();
        String _amount = amountTextField.getText();
        String _receiverBank = null;
        try {
            _receiverBank = myChoiceBox.getValue().toString();
        }
        catch (NullPointerException e){
            transactionErrLabel.setText("ERROR: Fill all the blank.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        String _toaccount = receiverAccTextfield.getText();
        HttpResponse<String> response = null;
        if(_fromaccount == null || _fromaccount.trim().isEmpty() ||
                _message == null || _message.trim().isEmpty() ||
                _amount == null || _amount.trim().isEmpty() ||
                _receiverBank == null || _receiverBank.trim().isEmpty() ||
                _toaccount == null || _toaccount.trim().isEmpty()){
            transactionErrLabel.setText("ERROR: Fill all the blank.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else if (_receiverBank == null){
            transactionErrLabel.setText("ERROR: Fill all the blank.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else if (_receiverBank != "HUST BANK") {
            transactionErrLabel.setText("ERROR: The bank is currently under\nmaintenance.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else if (!checkInteger(_amount)){
            transactionErrLabel.setText("ERROR: Amount must be an integer");
            transactionErrLabel.setVisible(true);
            amountTextField.clear();
        }
        else {
            transactionErrLabel.setVisible(false);
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://13.239.134.221:8080/api/v1/admin/transfer-between-user"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("Bearer %s", _token))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"fromAccount\":\"%s\"," +
                                        "\"toAccount\":\"%s\"," +
                                        "\"fund\":\"%s\"," +
                                        "\"message\":\"%s\" }", _fromaccount, _toaccount, _amount, _message)
                        ))
                        .build();
                System.out.println(String.format("{\"fromAccount\":\"%s\"," +
                        "\"toAccount\":\"%s\"," +
                        "\"fund\":\"%s\"," +
                        "\"message\":\"%s\"}", _fromaccount, _toaccount, _amount, _message));
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                System.out.println("DHA");
            }
            int response_code = response.statusCode();
            System.out.println(response.body());
            if (response_code == 403) {
                //Not enough Money
                transactionErrLabel.setText("ERROR: Not enough money");
                transactionErrLabel.setVisible(true);
                cleartransfer();
            } else if (response_code == 404) {
                //User not found
                transactionErrLabel.setText("ERROR: Receiver or Sender is not found");
                transactionErrLabel.setVisible(true);
                cleartransfer();
            }
            else {
                if (response_code == 200) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Transaction Successful");
                    alert.setHeaderText("Transfer Completed!");
                    alert.setContentText("Your transfer was processed successfully.\nThank you for using our service!");
                    // Thêm nút tùy chỉnh (nếu cần)
                    alert.getButtonTypes().setAll(ButtonType.OK);

                    // Thêm CSS để tùy chỉnh giao diện
                    alert.getDialogPane().getStylesheets().add(
                            getClass().getResource("/webapp/css/refer.css").toExternalForm()
                    );
                    alert.getDialogPane().getStyleClass().add("success-alert");

                    // Hiển thị alert
                    alert.showAndWait();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("The transaction has not been processed.");
                    alert.setHeaderText("Transfer Failed!");
                    alert.setContentText("Your transaction was unsuccessful due to Server error");
                    // Thêm nút tùy chỉnh (nếu cần)
                    alert.getButtonTypes().setAll(ButtonType.OK);
                    // Thêm CSS để tùy chỉnh giao diện
                    alert.getDialogPane().getStylesheets().add(
                            getClass().getResource("/webapp/css/refer.css").toExternalForm()
                    );
                    alert.getDialogPane().getStyleClass().add("invalid-otp-alert");

                    // Hiển thị alert
                    alert.showAndWait();
                }
                switchToTranfer(event);
            }
        }
    }

    public void cleartransfer(){
        senderTextfield.clear();
        decripstionTextField.clear();
        amountTextField.clear();
        receiverAccTextfield.clear();
        myChoiceBox.setValue(null);
    }

    public void depositFund(){
        depositErrLabel.setVisible(false);
        depositErrLabel.setTextFill(Color.RED);
        String _account = depositReceiver.getText();
        String _amount = amountDeposit.getText();
        if(_account == null || _account.trim().isEmpty()){
            depositErrLabel.setVisible(true);
            depositErrLabel.setText("Error: Account is empty");
        }
        else if (_amount == null || _amount.trim().isEmpty()){
            depositErrLabel.setTextFill(Color.RED);
            depositErrLabel.setVisible(true);
            depositErrLabel.setText("Error: Amount is empty");
        }
        else if (!checkInteger(_amount)){
            depositErrLabel.setTextFill(Color.RED);
            depositErrLabel.setVisible(true);
            depositErrLabel.setText("Error: Amount must be an integer");
        }
        else {
            HttpResponse<String> response = null;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://13.239.134.221:8080/api/v1/auth/buff-money"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("Bearer %s", _token))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"account\":\"%s\",\"fund\":\"%s\"}", _account, _amount)
                        ))
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }
            catch (Exception e){
                e.printStackTrace();
                depositErrLabel.setTextFill(Color.RED);
                depositErrLabel.setVisible(true);
                depositErrLabel.setText("Something went wrong");
            }
            if (response != null){
                int status_code = response.statusCode();
                if (status_code == 200){
                    depositErrLabel.setTextFill(Color.GREEN);
                    depositErrLabel.setText("Transfer Successful");
                    depositErrLabel.setVisible(true);
                    clearDeposit();
                }
                else{
                    depositErrLabel.setTextFill(Color.RED);
                    depositErrLabel.setVisible(true);
                    depositErrLabel.setText("Invalid Receiver");
                }
            }
            else {
                depositErrLabel.setTextFill(Color.RED);
                depositErrLabel.setVisible(true);
                depositErrLabel.setText("Server Error, try again later");
            }
        }
    }

    public void clearDeposit(){
        depositReceiver.clear();
        amountDeposit.clear();
    }

    public boolean checkInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void getHistoryTransaction(){
        String _account = keywordToSearch.getText();
        String _token = User.getCredential().getToken();
        String _startDate = _fromdate;
        String _endDate = _todate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.parse(_startDate, formatter);
        LocalDate endDate = LocalDate.parse(_endDate, formatter);
        if (startDate.isAfter(endDate)) {
            historyError.setVisible(true);
            historyError.setText("From Search Date need to be before To Search Date");
        }
        else {
            if(startDate.isBefore(LocalDate.of(2024, 1, 1)) || endDate.isAfter(LocalDate.of(2024, 12, 31))){
                historyError.setVisible(true);
                historyError.setText("Search Date need to be in 2024");
            }
            else if (_account == null || _account.trim().isEmpty()){
                historyError.setVisible(true);
                historyError.setText("Fill the Search.");
            }
            else {
                String reqEndpoint = "http://13.239.134.221:8080/api/v1/bank-api/check-banking-transition-date-range";
                String endpoint = String.format("%s?account=%s&startDate=%s&endDate=%s", reqEndpoint, _account, _startDate, _endDate);
                System.out.println(endpoint);
                HttpResponse<String> response = null ;
                // Get by range
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI(endpoint))
                            .header("Content-Type", "application/json")
                            .header("Authorization", String.format("Bearer %s", _token))
                            .GET()
                            .build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("response:" + response.body());
                }
                catch (Exception e) {
                    System.out.println("Error");
                }
                int response_code = response.statusCode();
                System.out.println("history" + response_code);
                if (response_code == 200) {
                    System.out.println(response.body());
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ArrayList<Transaction> transactionArrayList = objectMapper.readValue(response.body(), new TypeReference<ArrayList<Transaction>>() {
                        });
                        Collections.sort(transactionArrayList, new Comparator<Transaction>() {
                            @Override
                            public int compare(Transaction t1, Transaction t2) {
                                int dateTimeComparison = t1.getDateTime().compareTo(t2.getDateTime());
                                return dateTimeComparison;
                            }
                        });
                        ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactionArrayList);
                        // Set Cell Value Factories
                        timeColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
                        fromAccountColumn.setCellValueFactory(new PropertyValueFactory<>("fromAccount"));
                        toAccountColumn.setCellValueFactory(new PropertyValueFactory<>("toAccount"));
                        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

                        // Bind data to TableView
                        transactionTable.setItems(observableTransactions);
                    }
                    catch (Exception except) {
                        System.out.println("NULL");
                        except.printStackTrace();
                    }
                }
                else {
                    historyError.setVisible(true);
                    historyError.setText("Invalid account");
                }

            }
        }
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
