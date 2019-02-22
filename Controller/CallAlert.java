package Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CallAlert {
	public static void callAlert(String contentText) { //경고창
		Alert alert= new Alert(AlertType.INFORMATION);
		alert.setTitle("경고");
		alert.setHeaderText(contentText.substring(0,contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
	}	
}
