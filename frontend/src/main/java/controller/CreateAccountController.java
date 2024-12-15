package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Credential;
import model.User;

import java.time.LocalDate;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {
    @FXML
    private VBox Verify;
    @FXML
    private VBox registerScene;
    @FXML
    private TextField otpField1;
    @FXML
    private TextField otpField2;
    @FXML
    private TextField otpField3;
    @FXML
    private TextField otpField4;
    @FXML
    private TextField otpField5;
    @FXML
    private TextField otpField6;
    @FXML
    private VBox tranfer;
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
    private Label notificationLabel;
    @FXML
    private Label notificationOTP;
    @FXML
    private PasswordField newPwdField;
    @FXML
    private PasswordField confirmPwdField;

    private String notification;

    public void initialize(URL url, ResourceBundle rb) {
        Verify.setVisible(false);
        //registerScene.setVisible(true);
        //Verify.setMouseTransparent(true);
        Verify.setMouseTransparent(true);
        setupOTPInput();
    }
    private void setupOTPInput() {
        // Gọi hàm thiết lập sự kiện cho các ô
        setupTextFieldNavigation(null,otpField1, otpField2);
        setupTextFieldNavigation(otpField1,otpField2, otpField3);
        setupTextFieldNavigation(otpField2,otpField3, otpField4);
        setupTextFieldNavigation(otpField3,otpField4, otpField5);
        setupTextFieldNavigation(otpField4,otpField5, otpField6);
        setupTextFieldNavigation(otpField5,otpField6, null); // Field cuối cùng
    }

    public void clearOTP(){
        otpField1.clear();otpField2.clear();otpField3.clear();otpField4.clear();otpField5.clear();otpField6.clear();
    }

    private void setupTextFieldNavigation(TextField preField, TextField currentField, TextField nextField) {
        currentField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                // Chỉ cho phép nhập 1 ký tự
                currentField.setText(newValue.substring(0, 1));
            }

            if (!newValue.isEmpty() && nextField != null) {
                nextField.requestFocus(); // Chuyển sang ô tiếp theo
            }
        });

        currentField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    if (currentField.getText().isEmpty() && nextField != otpField1 && currentField != otpField1) {
                        preField.requestFocus(); // Quay lại ô trước nếu nhấn Backspace
                    }
                    break;
                default:
                    break;
            }
        });
    }
    public String getOTPCode() {
        return otpField1.getText() + otpField2.getText() + otpField3.getText() +
                otpField4.getText() + otpField5.getText() + otpField6.getText();
    }
    public void registerSubmit(ActionEvent event){
        String _password = newPwdField.getText();
        String _repassword = confirmPwdField.getText();
        System.out.println(_password + " " + _repassword);
        if (!_password.equals(_repassword)) {
            notificationLabel.setText("Your password you typed is mismatch. Please try again!");
            newPwdField.clear();
            confirmPwdField.clear();
        }
        else {
            String _account = userTextField.getText();
            String _email = emailTextField.getText();
            HttpResponse<String> response = null ;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://13.239.134.221:8080/api/v1/auth/register"))
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
            if (response.statusCode() == 200) {
                Verify.setVisible(true);
                registerScene.setVisible(false);
                registerScene.setMouseTransparent(true);
                Verify.setMouseTransparent(false);
            }
            else if(response.statusCode() == 401) {
                notificationLabel.setText("The account has already been registered. Please try with a different account.");
                emailTextField.clear();
                userTextField.clear();
            }
            else {
                System.out.println("dha" + response.statusCode());
            }
        }
    }

    public void verifyCode(ActionEvent event){
        String _account = userTextField.getText();
        String _email = emailTextField.getText();
        String _fname = fnameTextField.getText();
        String _lname = lnameTextField.getText();
        String _password = newPwdField.getText();
        String _address = addrTextField.getText();
        String _phone = phoneTextField.getText();
        String _createdate = LocalDate.now().toString();
        String _otp = getOTPCode();
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/auth/register-otp"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"account\":\"%s\"," +
                                            "\"firstName\":\"%s\"," +
                                            "\"lastName\":\"%s\"," +
                                            "\"email\":\"%s\"," +
                                            "\"password\":\"%s\"," +
                                            "\"address\":\"%s\","+
                                            "\"createDate\":\"%s\"," +
                                            "\"phoneNumber\":\"%s\"," +
                                            "\"otp\":\"%s\"}", _account,_fname, _lname, _email,
                                    _password, _address, _createdate, _phone, _otp)
                    ))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response.statusCode());
        if (response.statusCode() == 200) {
            try {
                String responseBody = response.body();
                ObjectMapper objectMapper = new ObjectMapper();
                Credential credential = objectMapper.readValue(responseBody, Credential.class);
                System.out.println(responseBody);
                User.setCredential(credential);
                SceneController sceneCotroller = new SceneController();
                sceneCotroller.switchToMainScene(event);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            clearOTP();
            notificationOTP.setText("Error: Wrong OTP. Try again.");
            notificationOTP.textFillProperty().set(Color.RED);
            notificationOTP.setVisible(true);
        }
    }

    public void resendOtp(ActionEvent event){
        HttpResponse<String> response = null;
        String _account = userTextField.getText();
        String _email = emailTextField.getText();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://13.239.134.221:8080/api/v1/auth/register"))
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
        if(response.statusCode() == 200) {
            clearOTP();
            notificationOTP.setText("Successfully send OTP.");
            notificationOTP.textFillProperty().set(Color.BLUE);
            notificationOTP.setVisible(true);
        }
        else {
            System.out.println("Resent ERROR " + response.statusCode());
        }
    }


    public void switchLoginScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToLogin(event);
    }
}
