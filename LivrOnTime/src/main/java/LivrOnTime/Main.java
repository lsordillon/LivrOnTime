package LivrOnTime;



import java.io.FileInputStream;

import controleur.AccueilControleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	public static AccueilControleur aController;
    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Accueil2.fxml"));
    	Parent root = loader.load();
    	aController = loader.getController();
    	primaryStage.getIcons().add(new Image(new FileInputStream("src/main/resources/img/livrontime-icon.jpg")));
        primaryStage.setTitle("LivrOnTime");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    } 
    

    public static void main(String[] args) {
        launch(args);
    }
}
