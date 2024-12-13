package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class ControllerOTP implements Initializable {
    private Consumer<String> onSubmitListener;
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
    private Button submit;
    public void initialize(URL url, ResourceBundle rb) {
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
    public void submit(){
        try{
            Stage stage = (Stage) submit.getScene().getWindow();
            if (onSubmitListener != null) {
                onSubmitListener.accept(getOTPCode());
            }
            stage.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void setOnSubmitListener(Consumer<String> listener) {
        this.onSubmitListener = listener;
    }
}
