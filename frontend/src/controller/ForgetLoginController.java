package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class ForgetLoginController implements Initializable {
    @FXML
    private AnchorPane head;
    @FXML
    private Label error;
    @FXML
    private Label notifi;
    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextField text3;
    public boolean check = true;
    public void initialize(URL url, ResourceBundle rb) {
        error.setVisible(false);
        notifi.setVisible(false);
    }

    public void switchLoginScene(ActionEvent event) throws IOException {
        SceneController sceneCotroller = new SceneController();
        sceneCotroller.switchToLogin(event);
    }

    public void sumbit(ActionEvent event){
        //check = false;
        if(check == true){
            notifi.setVisible(true);
            text1.getStyleClass().remove("error");
            text2.getStyleClass().remove("error");
            text3.getStyleClass().remove("error");
        }
        else {
            error.setVisible(true);
            text1.getStyleClass().add("error");
            text2.getStyleClass().add("error");
            text3.getStyleClass().add("error");
        }
    }
}
