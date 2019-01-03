package diplomski.controlleri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import diplomski.database.BazaPodataka;
import diplomski.entiteti.Godina;
import diplomski.glavna.Glavna;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class GodineController {

	@FXML
	public Label korisnikLabel;
	@FXML
	public TextField novaGodinaTextField;
	@FXML
	private TableView<Godina> godinaTable;
	@FXML
	private TableColumn<Godina, Integer> godinaColumn;

	public static Godina godinaSelected;

	@FXML
	public void initialize() {
		
		Integer godinaID = null;		
		Integer korisnikID = null;
		Integer godina = 0;
		String infoGodina = null;
		
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		
		List<Godina> listaGodina = new ArrayList<>();
		godinaColumn.setCellValueFactory(new PropertyValueFactory<Godina, Integer>("godina"));
		Connection veza = null;
		
		try {
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement popuniTablicu = veza
					.prepareStatement("SELECT * FROM RAZVOJ.GODINA WHERE KORISNIKID = ? ORDER BY GODINA");
			popuniTablicu.setInt(1, UlazController.korisnik.getKorisnikID());
			ResultSet rs = popuniTablicu.executeQuery();
			while (rs.next()) {
				Godina godine = new Godina (godinaID, korisnikID, godina, infoGodina);
				godinaID = rs.getInt("GODINAID");
				korisnikID = rs.getInt("KORISNIKID");
				godina = rs.getInt("GODINA");
				infoGodina = rs.getString("INFOGODINA");
				godine.setGodinaID(godinaID);
				godine.setKorisnikID(korisnikID);
				godine.setGodina(godina);
				godine.setInfoGodina(infoGodina);
				listaGodina.add(godine);
			}
			
			ObservableList<Godina> lista = FXCollections.observableArrayList(listaGodina);
			godinaColumn.setCellValueFactory(new PropertyValueFactory<>("godina"));
			godinaColumn.setStyle( "-fx-alignment: CENTER;\n"+ "-fx-font-size: 16;");
			
			godinaTable.setItems(lista);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				BazaPodataka.closeConnectionToDatabase(veza);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void dodajGodinuButtonClicked() {
		System.out.println("dodajGodinuButton clicked");
		Integer godina = 0;
		Integer godinaID = null;
		Connection veza = null;
		
		try {
		
			godina = Integer.parseInt(novaGodinaTextField.getText());
			
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement traziSlobodnuGodinu = veza.prepareStatement("SELECT GODINAID FROM RAZVOJ.GODINA WHERE GODINA = ? AND KORISNIKID = ?");
			traziSlobodnuGodinu.setInt(1, godina);
			traziSlobodnuGodinu.setInt(2, UlazController.korisnik.getKorisnikID());
			ResultSet rs = traziSlobodnuGodinu.executeQuery();
			while (rs.next()) {
				godinaID = rs.getInt("GODINAID");
			}

			if (godinaID == null) {
				if (Integer.toString(godina).length() == 4 && 1950 < godina && godina < 2016) {

					PreparedStatement preparedStatement = veza
							.prepareStatement("INSERT INTO RAZVOJ.GODINA "
									+ "(KORISNIKID, GODINA) VALUES (?, ?)");
					preparedStatement.setInt(1,
							UlazController.korisnik.getKorisnikID());
					preparedStatement.setInt(2, godina);
					preparedStatement.execute();

					System.out.println("Dodana je godina: " + godina);
				}
				if (Integer.toString(godina).length() != 4){
					System.out
							.println("Godina je èetveroznamenkasti cijeli broj!");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText(null);
				alert.setContentText("Godina je èetveroznamenkasti cijeli broj!");

				alert.showAndWait();
				}
				if (Integer.toString(godina).length() == 4 && godina <= 1950) {
					System.out
					.println("Unesite godinu veæu od 1950!");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Evidencija Polja");
					alert.setHeaderText(null);
					alert.setContentText("Unesite godinu veæu od 1950!");
					alert.showAndWait();
				}
				if (Integer.toString(godina).length() == 4 && 2016 <= godina) {
					System.out
					.println("Unesite godinu manju od 2016!");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Evidencija Polja");
					alert.setHeaderText(null);
					alert.setContentText("Unesite godinu manju od 2016!");
					alert.showAndWait();
				}
			} else {
				System.out.println("Godina postoji!");
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText(null);
				alert.setContentText("Godina postoji!");
				alert.showAndWait();
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("Unesite èetveroznamenkasti cijeli broj, ne slova!");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Unesite èetveroznamenkasti cijeli broj, ne slova!");

			alert.showAndWait();
		} finally {
			try {
				BazaPodataka.closeConnectionToDatabase(veza);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		initialize();
	}

	
	public void korisnikLabelClicked() {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Korisnièko ime: " + UlazController.korisnik.getKorisnickoIme()+ 
				"\nIme: "+ UlazController.korisnik.getIme() + 
				"\nPrezime: " + UlazController.korisnik.getPrezime() + 
				"\nOPG ili poduzeæe: " + UlazController.korisnik.getOpg());

		alert.showAndWait();
		
	}
	public void odjavaClicked() {
		System.out.println("odjava clicked");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Sigurno se želite odjaviti?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA) {
			try {
				Parent root = FXMLLoader.load(getClass().getResource(
						"../fxml/Ulaz.fxml"));
				Scene scene = new Scene(root);
				Stage stage = Glavna.window;
				stage.close();
				stage.setScene(scene);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
			UlazController.korisnik.setKorisnikID(null);
			UlazController.korisnik.setKorisnickoIme(null);
			UlazController.korisnik.setLozinka(null);
			UlazController.korisnik.setIme(null);
			UlazController.korisnik.setPrezime(null);
			UlazController.korisnik.setOpg(null);
		} else {
			// natrag na godine
		}

	}
	
	
	public void godinaTableClick() {
		System.out.println("godinaTable clicked");
		Godina godine = godinaTable.getSelectionModel().getSelectedItem();
		
		
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li uæi u godinu " + godine.getGodina() +"?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA){
			Integer godinaID = null;		
			Integer korisnikID = null;
			Integer godina = 0;
			String infoGodina = null;
			
			godinaSelected = new Godina (godinaID, korisnikID, godina, infoGodina);
			godinaSelected = godine;
			System.out.println(godinaSelected.getGodina());
			System.out.println(godinaSelected.getGodinaID());
			System.out.println(godinaSelected.getKorisnikID());
			
			try{
				Parent root = FXMLLoader.load(getClass().getResource("../fxml/Izbornik.fxml"));
				Scene scene = new Scene (root);
				Stage stage = Glavna.window;
				stage.close();
				stage.setScene(scene);
				stage.show();
				
				}
				catch (Exception e){
					e.printStackTrace();
				}
			
			
		} else {
		    // natrag na izbornik godina
		}
	}
}
