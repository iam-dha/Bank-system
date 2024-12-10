package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Credential;
import model.Notification;
import model.Transaction;
import model.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private VBox tranfer;
    @FXML
    private VBox setting;
    @FXML
    private VBox history;
    @FXML
    private HBox overview;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private AreaChart<String, Number> areaChart;
    @FXML
    private HBox otpBox;
    @FXML
    private Label currentBalance;
    @FXML
    private Button send;
    @FXML
    private CategoryAxis xAxis; // Trục X, biểu diễn các tháng
    @FXML
    private NumberAxis yAxis; // Trục Y, biểu diễn số tiền
    @FXML
    private Label senderLabel;
    @FXML
    private TextField decripstionTextFiel;
    @FXML
    private TextField amountTextField;
    @FXML
    private ChoiceBox myChoiceBox;
    @FXML
    private TextField receiverAccTextfield;
    @FXML
    private Label transactionErrLabel;
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
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private Label historyError;
    @FXML
    private Label notification1;
    @FXML
    private Label notification2;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField fnameTextField;
    @FXML
    private TextField lnameTextField;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField addrTextField;
    @FXML
    private Label changePassLabel;
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    private Transaction tran;
    private User user;
    private Credential credetial;
    private String _fromdate = "01/01/2024";
    private String _todate = "10/12/2024";


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
    public void getHistoryTransaction() {
        Credential credential = User.getCredential();
        String _token = credential.getToken();
        String _account = user.getAccount();
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
            else{
                String reqEndpoint = "http://3.27.209.207:8080/api/v1/bank-api/check-banking-transition-date-range";
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
                if (response_code == 200) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ArrayList<Transaction> transactionArrayList = objectMapper.readValue(response.body(), new TypeReference<ArrayList<Transaction>>() {
                        });
                        Collections.sort(transactionArrayList, new Comparator<Transaction>() {
                            @Override
                            public int compare(Transaction t1, Transaction t2) {
                                // First compare by dateTime
                                int dateTimeComparison = t1.getDateTime().compareTo(t2.getDateTime());
                                if (dateTimeComparison != 0) {
                                    return dateTimeComparison;
                                }

                                // If dateTime is equal, compare by time
                                return t1.getTime().compareTo(t2.getTime());
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
                    catch (Exception except){
                        System.out.println("NULL");
                    }
                }
                else {
                    historyError.setVisible(true);
                    historyError.setText("Something went wrong");
                }
            }
        }
    }

    public void settingUser(){
        fnameTextField.setText(user.getFirstname());
        fnameTextField.setMouseTransparent(true);
        lnameTextField.setText(user.getLastname());
        lnameTextField.setMouseTransparent(true);
        emailTextField.setText(user.getEmail());
        emailTextField.setMouseTransparent(true);
        userTextField.setText(user.getAccount());
        userTextField.setMouseTransparent(true);
        phoneTextField.setText(user.getPhonenumber());
        phoneTextField.setMouseTransparent(true);
        addrTextField.setText(user.getAddress());
        addrTextField.setMouseTransparent(true);
    }

    public void setNotification(){
        HttpResponse<String> response = null;
        ArrayList<Notification> notificationArrayList = null;
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://3.27.209.207:8080/api/v1/auth/get-notification"))
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper objectMapper = new ObjectMapper();
            notificationArrayList = objectMapper.readValue(response.body(), new TypeReference<ArrayList<Notification>>() {
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Notification _notification1 = notificationArrayList.get(0);
        Notification _notification2 = notificationArrayList.get(1);
        notification1.setText(String.format("%s\n%s\n%s", _notification1.getDateTime(), _notification1.getTitle(), _notification1.getMessage()));
        notification2.setText(String.format("%s\n%s\n%s", _notification2.getDateTime(), _notification2.getTitle(), _notification2.getMessage()));
    }

    public void initialize(URL url, ResourceBundle rb) {
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
        setting.setMouseTransparent(true);
        setting.setVisible(false);
        initialize();
        switchToOverview();
        fromDate.setOnAction(e -> {
            LocalDate selectedDate = fromDate.getValue(); // Get selected date
            if (selectedDate != null) {
                // Format the date to "dd-MM-yyyy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                _fromdate = selectedDate.format(formatter);
            } else {
                System.out.println("No date selected.");
            }
        });
        toDate.setOnAction(e -> {
            LocalDate selectedDate = toDate.getValue(); // Get selected date
            if (selectedDate != null) {
                // Format the date to "dd-MM-yyyy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                _todate = selectedDate.format(formatter);
            } else {
                System.out.println("No date selected.");
            }
        });
    }
    public void switchToTranfer(ActionEvent e){
        getUserInformation();
        cleartransfer();
        transactionErrLabel.setVisible(false);
        senderLabel.setText(user.getAccount());
        currentBalance.setText(user.getFund());
        tranfer.setVisible(true);
        tranfer.setMouseTransparent(false);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        setting.setMouseTransparent(true);
        setting.setVisible(false);
    }
    public void switchToSetting(){
        getUserInformation();
        settingUser();
        currentPassword.clear();
        newPassword.clear();
        setting.setVisible(true);
        setting.setMouseTransparent(false);
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        changePassLabel.setVisible(false);
    }
    public void switchToOverview(){
        setNotification();
        getUserInformation();
        balanceLabel.setText(user.getFund());
        welcomeLabel.setText("Welcome, " + user.getFirstname());
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
        history.setVisible(false);
        history.setMouseTransparent(true);
        setting.setMouseTransparent(true);
        setting.setVisible(false);
    }
    public void switchToHistory(){
        historyError.setVisible(false);
        getUserInformation();
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(true);
        history.setMouseTransparent(false);
        fromDate.setValue(LocalDate.of(2024, 1, 1));
        toDate.setValue(LocalDate.now());
        setting.setMouseTransparent(true);
        setting.setVisible(false);
    }

    public void cleartransfer(){
        decripstionTextFiel.clear();
        amountTextField.clear();
        receiverAccTextfield.clear();
        myChoiceBox.setValue(null);
    }

    public void chagePassword(){
        String _token = User.getCredential().getToken();
        String _account = User.getCredential().getAccount();
        String _current = currentPassword.getText();
        String _new = newPassword.getText();
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://3.27.209.207:8080/api/v1/user/change-password"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", _token))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"account\":\"%s\"," +
                                    "\"currentPassword\":\"%s\"," +
                                    "\"newPassword\":\"%s\" }", _account, _current, _new)
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (response.statusCode() == 200){
            changePassLabel.setVisible(true);
            changePassLabel.setText("Change password successfully");
            changePassLabel.textFillProperty().set(Color.GREEN);
        }
        else {
            changePassLabel.setVisible(true);
            changePassLabel.setText("Something went wrong.");
            changePassLabel.textFillProperty().set(Color.RED);
        }
    }

    public void initialize() {

        //areaChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        myChoiceBox.getItems().addAll("HUST BANK", "NEU BANK", "HNUE BANK");
        //myChoiceBox.setValue("Option 1"); // Giá trị mặc định
        // Tên các tháng
        areaChart.setVerticalGridLinesVisible(false);
        System.out.println(areaChart.getXAxis().getStyleClass());
        System.out.println(areaChart.getStyle());
        // Cấu hình trục X (Months)
        xAxis.setCategories(FXCollections.observableArrayList(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ));

        // Cấu hình trục Y (Amount)
        yAxis.setTickUnit(10000); // Khoảng cách giữa các giá trị
        yAxis.setAutoRanging(true); // Tắt chế độ tự động tính phạm vi
        //yAxis.setLowerBound(0); // Giá trị thấp nhất
        //yAxis.setUpperBound(3000); // Giá trị cao nhất
        yAxis.setTickMarkVisible(false);  // Ẩn các dấu chia dọc trên trục Y
        yAxis.setMinorTickVisible(false);
        xAxis.setTickMarkVisible(false);  // Ẩn các dấu chia dọc trên trục Y
        xAxis.setTickMarkVisible(false);
        // Dữ liệu mẫu
        double[] income = {1200, 1500, 1700, 1600, 1800, 2000, 2100, 1900, 2200, 2400, 2300, 2500};
        double[] expense = {800, 1000, 1900, 1100, 1300, 2300, 1500, 1200, 2600, 1000, 900, 950};

        // Tạo Series cho Tiền vào (Income)
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        // Tạo Series cho Tiền ra (Expense)
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        // Thêm dữ liệu vào Series
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            XYChart.Data<String, Number> incomeData = new XYChart.Data<>(months[i], income[i]);
            XYChart.Data<String, Number> expenseData = new XYChart.Data<>(months[i], expense[i]);
            incomeSeries.getData().add(incomeData);
            expenseSeries.getData().add(expenseData);
        }
        for (XYChart.Data<String, Number> data : incomeSeries.getData()) {
            // Tạo Tooltip cho incomeData
            Tooltip incomeTooltip = new Tooltip("Income: " + data.getYValue());
            Tooltip.install(data.getNode(), incomeTooltip);
        }

        for (XYChart.Data<String, Number> data : expenseSeries.getData()) {
            Tooltip expenseTooltip = new Tooltip("Expense: " + data.getYValue());
            Tooltip.install(data.getNode(), expenseTooltip);
        }

        // Thêm Series vào AreaChart
        areaChart.getData().addAll(incomeSeries, expenseSeries);
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
    @FXML
    public void popOtp(ActionEvent event) {
        String _token = User.getCredential().getToken();
        System.out.println(_token);
        String _fromaccount = senderLabel.getText();
        String _message = decripstionTextFiel.getText();
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
        if (_receiverBank == null){
            transactionErrLabel.setText("ERROR: Fill all the blank.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else if (_receiverBank != "HUST BANK") {
            transactionErrLabel.setText("ERROR: The bank is currently under\nmaintenance.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else if(_message == null || _message.trim().isEmpty() ||
                _amount == null || _amount.trim().isEmpty() ||
                _receiverBank == null || _receiverBank.trim().isEmpty() ||
                _toaccount == null || _toaccount.trim().isEmpty()){
            transactionErrLabel.setText("ERROR: Fill all the blank.");
            transactionErrLabel.setVisible(true);
            cleartransfer();
        }
        else {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://3.27.209.207:8080/api/v1/bank-api/banking"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", String.format("Bearer %s", _token))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                String.format("{\"fromAccount\":\"%s\"," +
                                        "\"toAccount\":\"%s\"," +
                                        "\"fund\":\"%s\"," +
                                        "\"message\":\"%s\" }", _fromaccount, _toaccount, _amount, _message)
                        ))
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                System.out.println("DHA");
            }
            int response_code = response.statusCode();
            if (response_code == 401) {
                //Not enough Money
                transactionErrLabel.setText("ERROR: Not enough money");
                transactionErrLabel.setVisible(true);
                cleartransfer();
            } else if (response_code == 404) {
                //User not found
                transactionErrLabel.setText("ERROR: Receiver is not found");
                transactionErrLabel.setVisible(true);
                cleartransfer();
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/changeOtp.fxml"));
                    //Parent root = FXMLLoader.load(getClass().getResource("/views/changeOtp.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    ControllerOTP otpController = loader.getController();
                    otpController.setOnSubmitListener(otp -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        System.out.println("Received OTP: " + otp.length()); // Xử lý OTP
                        System.out.println(decripstionTextFiel.getText()); //debug
                        // Send request to Process payment
                        HttpResponse<String> transaction_res = null;
                        try {
                            HttpClient client = HttpClient.newHttpClient();
                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(new URI("http://3.27.209.207:8080/api/v1/bank-api/banking-otp"))
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", String.format("Bearer %s", _token))
                                    .POST(HttpRequest.BodyPublishers.ofString(
                                            String.format("{\"fromAccount\":\"%s\"," +
                                                    "\"toAccount\":\"%s\"," +
                                                    "\"fund\":\"%s\"," +
                                                    "\"message\":\"%s\", \"otp\":\"%s\"}", _fromaccount, _toaccount, _amount, _message, otp)
                                    ))
                                    .build();
                            transaction_res = client.send(request, HttpResponse.BodyHandlers.ofString());
                        } catch (Exception e) {
                            System.out.println("DHA");
                        }
                        int transaction_response = transaction_res.statusCode();
                        if (transaction_response == 200) {
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
                        } else {
                            System.out.println(transaction_response);
                            alert.setTitle("The transaction was not approved.");
                            alert.setHeaderText("Transfer Failed!");
                            alert.setContentText("Your transaction was unsuccessful due to an INVALID OTP code.\nTry another transaction.");
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
                    });
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(send.getScene().getWindow());
                    //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
