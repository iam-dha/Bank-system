package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox tranfer;
    @FXML
    private VBox history;
    @FXML
    private HBox overview;
    public void initialize(URL url, ResourceBundle rb) {
        tranfer.setVisible(false);
        tranfer.setMouseTransparent(true);
        history.setVisible(false);
        history.setMouseTransparent(true);
        overview.setVisible(true);
        overview.setMouseTransparent(false);
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




}
