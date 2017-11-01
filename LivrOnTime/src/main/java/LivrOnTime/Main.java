package LivrOnTime;



import controller.AccueilController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static AccueilController aController;
    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Accueil.fxml"));
    	Parent root = loader.load();
    	aController = loader.getController();
        primaryStage.setTitle("LivrOnTime");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    } 
    


    public static void main(String[] args) {
        launch(args);
    }
}
