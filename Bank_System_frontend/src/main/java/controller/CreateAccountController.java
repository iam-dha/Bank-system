package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
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
    public void VerifyCode(){
        Verify.setVisible(true);
        registerScene.setVisible(false);
        registerScene.setMouseTransparent(true);
        Verify.setMouseTransparent(false);
    }
    public void switchTranfer(){

    }
    public void switchLoginScene(ActionEvent event) {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToLogin(event);
    }
}
