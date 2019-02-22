package application;

import Controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader= new FXMLLoader(getClass().getResource("../View/login.fxml"));
		Parent root=loader.load();
		RootController rootController=loader.getController();
		rootController.stage=stage;
		Scene scene= new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
