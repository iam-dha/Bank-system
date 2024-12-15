package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneController {

    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public void switchToLogin(ActionEvent event)  {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/views/Login.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            //String css = SceneController.class.getClass().getResource("/resources/css/style.css").toExternalForm();
            //scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.setTitle("HUST Banking");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToForgetLogin(ActionEvent event)  {
        try{
            root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/views/forgetLogin.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

           // String css = SceneController.class.getClassLoader().getResource("/webapp/css/style.css").toExternalForm();
           // scene.getStylesheets().add(css);


            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void switchToCreateAccount(ActionEvent event)  {
        try{
            root = FXMLLoader.load(SceneController.class.getResource("/views/createAccount.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            // String css = SceneController.class.getClassLoader().getResource("/webapp/css/style.css").toExternalForm();
            // scene.getStylesheets().add(css);


            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void switchToMainScene(ActionEvent event)  {
        try{
            root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/views/mainScene.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            String css = Objects.requireNonNull(SceneController.class.getResource("/webapp/css/create.css")).toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToAdminScene(ActionEvent event){
        try{
            root = FXMLLoader.load(Objects.requireNonNull(SceneController.class.getResource("/views/adminScene.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
