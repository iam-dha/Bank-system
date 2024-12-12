package application;
import controller.ControllerLogin;
import javafx.application.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
        System.out.println("Hello world!");
    }

    @Override
    public void start(Stage stage) {
        try {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/Login.fxml")));
            Scene scene = new Scene(root);
            //String css = this.getClass().getResource("webapp/css/style.css").toExternalForm();
            //scene.getStylesheets().add(css);
            stage.setScene(scene);
            //stage.setWidth(1080);
            //stage.setHeight(768);
            stage.setTitle("HUST Banking");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}