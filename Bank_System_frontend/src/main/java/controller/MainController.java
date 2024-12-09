package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox tranfer;
    @FXML
    private VBox history;
    @FXML
    private HBox overview;

    private String OTP;
    public void initialize(URL url, ResourceBundle rb) {
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
        initialize();
    }
    public void switchToTranfer(){
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
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        overview.setVisible(false);
        overview.setMouseTransparent(true);
        history.setVisible(true);
        history.setMouseTransparent(false);
    }
    @FXML
    private TextField decripstionTextFiel;
    private String test;
    @FXML
    private AreaChart<String, Number> areaChart;
    @FXML
    private HBox otpBox;
    @FXML
    private Button send;
    @FXML
    private CategoryAxis xAxis; // Trục X, biểu diễn các tháng

    @FXML
    private NumberAxis yAxis; // Trục Y, biểu diễn số tiền
    @FXML
    private ChoiceBox myChoiceBox;

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
    public void popOtp(ActionEvent event)  {
        try{
            FXMLLoader loader =new FXMLLoader(getClass().getResource("/views/changeOtp.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("/views/changeOtp.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            ControllerOTP otpController = loader.getController();
            otpController.setOnSubmitListener(otp -> {
                System.out.println("Received OTP: " + otp); // Xử lý OTP
                decripstionTextFiel.setText(otp);
//                HttpClient client = HttpClient.newHttpClient();
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(new URI("http://localhost:8080/api/v1/auth/register"))
//                        .POST(HttpRequest.BodyPublishers.ofString(
//                                String.format("{\"account\":\"%s\", \"password\":\"%s\"}", user_name, password)
//                        ))
//                        .GET()
//                        .build();
//                    try {
//
//                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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
