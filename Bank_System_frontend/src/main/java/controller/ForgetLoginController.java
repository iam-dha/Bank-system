package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class ForgetLoginController implements Initializable {
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
    private TextField newPassword;
    @FXML
    private TextField confirmPassword;
    @FXML
    private Label error;
    @FXML
    private HBox succesNoti;
    @FXML
    private Label warningLabel;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private CheckBox checkBox;
    @FXML
    private VBox forgotScene;
    @FXML
    private VBox VerifyScene;
    @FXML
    private VBox newPasswordSceen;

    private String _account;
    private String _newpassword;
    private String _email;

    public void initialize(URL url, ResourceBundle rb) {
        error.setVisible(false);
        forgotScene.setVisible(true);
        forgotScene.setMouseTransparent(false);
        VerifyScene.setVisible(false);
        VerifyScene.setMouseTransparent(true);
        newPasswordSceen.setVisible(false);
        newPasswordSceen.setMouseTransparent(true);
        succesNoti.setVisible(false);
        setupOTPInput();
    }

    public void switchLoginScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToLogin(event);
    }

    public void submit(ActionEvent event){
        if(checkBox.isSelected()){
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
                System.out.println(response.statusCode());
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
                                    String.format("{\"account\" : \"%s\", \"email\" : \"%s\"}", _account, _email)
                            ))
                            .build();
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response.statusCode());
                }
                catch (Exception e){
                    error.setText("Something went wrong. Try again later!");
                    error.setVisible(true);
                }
                if (response.statusCode() == 200) {
                    error.setVisible(false);
                    forgotScene.setVisible(false);
                    forgotScene.setMouseTransparent(true);
                    VerifyScene.setVisible(false);
                    VerifyScene.setMouseTransparent(true);
                    newPasswordSceen.setVisible(true);
                    newPasswordSceen.setMouseTransparent(false);
                }
            }
            else {
                error.setText("We couldn't find an account associated with this account. Try again");
                error.setVisible(true);
            }
        }
        else {
            error.setText("NOTICE: Make sure that you agree to the terms.");
            error.setVisible(true);
            userTextField.getStyleClass().add("error");
            emailTextField.getStyleClass().add("error");
        }
    }

    public void clearOTP(){
        otpField1.clear();otpField2.clear();otpField3.clear();otpField4.clear();otpField5.clear();otpField6.clear();
    }

    public void changePassSubmit(){
        String _password = newPassword.getText();
        String _confirmPassword = confirmPassword.getText();

        if(!_password.equals(_confirmPassword)){
            warningLabel.setText("Your password you typed is mismatch.\nPlease try again!");
        }
        else {
            _newpassword = _password;
            forgotScene.setVisible(false);
            forgotScene.setMouseTransparent(true);
            VerifyScene.setVisible(true);
            VerifyScene.setMouseTransparent(false);
            newPasswordSceen.setVisible(false);
            newPasswordSceen.setMouseTransparent(true);
        }

    }

    public void resendOtp(){
        HttpResponse<String> response = null;
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
            System.out.println(response.statusCode());
        }
        catch (Exception e){
            error.setText("Something went wrong. Try again later!");
            error.setVisible(true);
        }

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

    private void setupOTPInput() {
        // Gọi hàm thiết lập sự kiện cho các ô
        setupTextFieldNavigation(null,otpField1, otpField2);
        setupTextFieldNavigation(otpField1,otpField2, otpField3);
        setupTextFieldNavigation(otpField2,otpField3, otpField4);
        setupTextFieldNavigation(otpField3,otpField4, otpField5);
        setupTextFieldNavigation(otpField4,otpField5, otpField6);
        setupTextFieldNavigation(otpField5,otpField6, null); // Field cuối cùng
    }

    public String getOTPCode() {
        return otpField1.getText() + otpField2.getText() + otpField3.getText() +
                otpField4.getText() + otpField5.getText() + otpField6.getText();
    }

    public void verifyCode(ActionEvent event){
        error.setVisible(false);
        String _otp = getOTPCode();
        HttpResponse<String> response = null ;
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://3.27.209.207:8080/api/v1/auth/forget-password-otp"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("{\"account\" : \"%s\",\"newPassword\": \"%s\",\"otp\": \"%s\" }", _account, _newpassword, _otp)
                    ))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception e){
            System.out.println("hehe");
        }
        if (response.statusCode() == 200) {
            succesNoti.setVisible(true);
            VerifyScene.setMouseTransparent(true);
        }

        else if (response.statusCode() == 500) {
            error.setText("Error: Something went wrong. Please try again.");
            clearOTP();
            error.setVisible(true);
        }

        else {
            error.setText("Error: Invalid OTP CODE. Please try again.");
            clearOTP();
            error.setVisible(true);
        }

    }

}