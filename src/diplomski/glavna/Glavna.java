package diplomski.glavna;

import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;


public class Glavna extends Application {
	
	
	public static Stage window;
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("../fxml/Ulaz.fxml"));
		primaryStage.setTitle("Evidencija Polja");
		primaryStage.setScene(new Scene(root, 500, 350));
		primaryStage.setResizable(false);
		primaryStage.show();
		window = primaryStage;
		window.setOnCloseRequest(e -> {
			e.consume();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Želite li sigurno izaæi iz programa?");
			ButtonType buttonDA = new ButtonType("Da");
			ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonDA, buttonNE);
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonDA){
				Stage stage = Glavna.window;
				stage.close();
			} else {
			    // izlaz iz programa
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
