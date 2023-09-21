package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(final Stage primaryStage) {
	    try {
	    	//Initilisation de la fenêtre
	        final URL url = getClass().getResource("../design/interface.fxml");
	        final FXMLLoader fxmlLoader = new FXMLLoader(url);
	        final AnchorPane root = (AnchorPane) fxmlLoader.load();
	        //Taille de la fenêtre
	        final Scene scene = new Scene(root, 1600, 960);
	        scene.getRoot().requestFocus();
			primaryStage.setScene(scene);
			//Nom de la fenêtre
		    primaryStage.setTitle("OriieFlamme");
		    //Icone de la fenêtre
		    primaryStage.getIcons().add(new Image("/design/Icone/png/IIE 64.png"));
		    primaryStage.show();
		    
	      } catch (IOException ex) {
	        System.err.println("Erreur au chargement: " + ex);
	      }
	    }
	
	public static void main(String[] args) {
		launch(args);
	}

}
