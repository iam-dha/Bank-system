package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Credential;
import model.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
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


    private User user;
    private Credential credetial;
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

    private String OTP;
    public void initialize(URL url, ResourceBundle rb) {
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
        initialize();
        switchToOverview();
    }
    public void switchToTranfer(ActionEvent e){
        getUserInformation();
        cleartransfer();
        senderLabel.setText(user.getAccount());
        currentBalance.setText(user.getFund());
        tranfer.setVisible(true);
        tranfer.setMouseTransparent(false);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
    }
    public void switchToOverview(){
        getUserInformation();
        balanceLabel.setText(user.getFund());
        welcomeLabel.setText("Welcome, " + user.getFirstname());
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

    public void cleartransfer(){
        decripstionTextFiel.clear();
        amountTextField.clear();
        receiverAccTextfield.clear();
        myChoiceBox.setValue(null);
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
        double[] expense = {800, 1000, 900, 1100, 1300, 1900, 1500, 1200, 2600, 1000, 900, 950};

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

            //incomeSeries.getData().add(incomeData);
            //expenseSeries.getData().add(expenseData);

        // Thêm Series vào AreaChart
        areaChart.getData().addAll(incomeSeries, expenseSeries);
    }
    @FXML
    public void popOtp(ActionEvent event) {
        String _token = User.getCredential().getToken();
        System.out.println(_token);
        String _fromaccount = senderLabel.getText();
        String _message = decripstionTextFiel.getText();
        String _amount = amountTextField.getText();
        String _receiverBank = myChoiceBox.getValue().toString();
        String _toaccount = receiverAccTextfield.getText();
        HttpResponse<String> response = null;
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
        if (response.statusCode() == 400) {
            System.out.println(response.statusCode());
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
                    int response_code = transaction_res.statusCode();
                    if (response_code == 200) {
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
                    else if (response_code != 200) {
                        System.out.println(response_code);
                        alert.setTitle("The transaction was not approved.");
                        alert.setHeaderText("Transfer Failed!");
                        alert.setContentText("Your transaction was unsuccessful due to an incorrect OTP code.\nTry another transaction.");
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
