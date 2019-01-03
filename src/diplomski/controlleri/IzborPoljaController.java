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
import diplomski.entiteti.Polje;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class IzborPoljaController {
	
	@FXML
	public Label korisnikLabel;

	@FXML
	public Label godinaLabel;
	
	@FXML
	public TextField imePoljaTextField;
	
	@FXML
	public TextField lokacijaPoljaTextField;
	
	@FXML
	public TextField povrsinaPoljaTextField;
	
	@FXML
	private TableView<Polje> poljaTable;
	
	@FXML
	private TableColumn<Polje, String> poljaColumn;
	
	
	public static Polje poljeSelected;
	
	@FXML
	public void initialize() {
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		godinaLabel.setText(GodineController.godinaSelected.getGodina()
				.toString());
		
		Integer poljeID = null;
		Integer godinaID = null;
		String imePolja = null;
		String lokacijaPolja = null;
		Double povrsinaPolja = null;
		Double prinos = null;
		Double cijenaOtkupa = null;
		Double potpora = null;
		String info = null;
		List<Polje> listaPolja = new ArrayList<>();
		poljaColumn.setCellValueFactory(new PropertyValueFactory<Polje, String>("godina"));
		Connection veza = null;
		
		try {
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement popuniTablicu = veza
					.prepareStatement("SELECT * FROM RAZVOJ.POLJE WHERE GODINAID = ?");
			popuniTablicu.setInt(1, GodineController.godinaSelected.getGodinaID());
			ResultSet rs = popuniTablicu.executeQuery();
			while (rs.next()) {
				Polje polje = new Polje (poljeID, godinaID, imePolja, lokacijaPolja, povrsinaPolja, prinos, cijenaOtkupa, potpora, info);
				poljeID = rs.getInt("POLJEID");
				godinaID = rs.getInt("GODINAID");
				imePolja = rs.getString("IMEPOLJA");
				lokacijaPolja = rs.getString("LOKACIJAPOLJA");
				povrsinaPolja = rs.getDouble("POVRSINAPOLJA");
				prinos = rs.getDouble("PRINOS");
				cijenaOtkupa = rs.getDouble("CIJENAOTKUPA");
				info = rs.getString("INFO");
				potpora = rs.getDouble("POTPORA");
				polje.setPoljeID(poljeID);
				polje.setGodinaID(godinaID);
				polje.setImePolja(imePolja);
				polje.setLokacijaPolja(lokacijaPolja);
				polje.setPovrsinaPolja(povrsinaPolja);
				polje.setPrinos(prinos);
				polje.setCijenaOtkupa(cijenaOtkupa);
				polje.setInfo(info);
				polje.setPotpora(potpora);
				listaPolja.add(polje);
			}
			
			ObservableList<Polje> lista = FXCollections.observableArrayList(listaPolja);
			poljaColumn.setCellValueFactory(new PropertyValueFactory<>("imePolja"));
			poljaColumn.setStyle( "-fx-alignment: CENTER;\n"+ "-fx-font-size: 16;");
			
			poljaTable.setItems(lista);
			
			
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
	
	public void godinaLabelClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Godina: "
				+ GodineController.godinaSelected.getGodina()
				+ "\nInfo o godini: "
				+ GodineController.godinaSelected.getInfoGodina());

		alert.showAndWait();
	}
	
	public void korisnikLabelClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Korisnièko ime: "
				+ UlazController.korisnik.getKorisnickoIme() + "\nIme: "
				+ UlazController.korisnik.getIme() + "\nPrezime: "
				+ UlazController.korisnik.getPrezime() + "\nOPG ili poduzeæe: "
				+ UlazController.korisnik.getOpg());

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

			GodineController.godinaSelected.setGodinaID(null);
			GodineController.godinaSelected.setKorisnikID(null);
			GodineController.godinaSelected.setGodina(null);
			GodineController.godinaSelected.setInfoGodina(null);
		} else {
			// natrag na godine
		}
	}
	public void spremiButtonClicked() {
		System.out.println("spremiButton clicked");
		
		Connection veza = null;
		try {
		String imePolja = imePoljaTextField.getText().trim();
		String lokacijaPolja = lokacijaPoljaTextField.getText().trim();
		
		Double povrsinaPolja = Double.parseDouble(povrsinaPoljaTextField.getText());
		
		if (imePolja.equals("") || lokacijaPolja.equals("") || povrsinaPolja == null) {
			System.out.println("Korisnik treba obavezno unijeti ime polja, lokaciju polja i površinu polja!! ");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText("Korisnik treba obavezno unijeti ime polja, lokaciju polja i površinu polja!! ");
			alert.setContentText("Provjerite jeste li unijeli toèno podatke polja.");

			alert.showAndWait();
		}else{
		
		
		
		
			
			veza = BazaPodataka.connectToDatabase();

			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO RAZVOJ.POLJE "
							+ "(GODINAID, IMEPOLJA, LOKACIJAPOLJA, POVRSINAPOLJA) VALUES (?, ?, ?, ?)");
			preparedStatement.setInt(1, GodineController.godinaSelected.getGodinaID());
			preparedStatement.setString(2, imePolja);
			preparedStatement.setString(3, lokacijaPolja);
			preparedStatement.setDouble(4, povrsinaPolja);
			preparedStatement.execute();

			imePoljaTextField.setText(null);
			lokacijaPoljaTextField.setText(null);
			povrsinaPoljaTextField.setText(null);
		}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Unesite broj za površinu polja! Ukoliko je decimalan koristite toèku umjesto zareza.");

			alert.showAndWait();
		}

		finally{
			try{
					BazaPodataka.closeConnectionToDatabase(veza);
			} catch(SQLException ex) {
			ex.printStackTrace();
			 }
			}
		
		initialize();
	}
	
	public void promjeniOdabranoButtonClicked() {
		System.out.println("promjeniOdabrano clicked");
		
		Polje polje = poljaTable.getSelectionModel().getSelectedItem();
		String imePolja = imePoljaTextField.getText();
		String lokacijaPolja = lokacijaPoljaTextField.getText();
		Double povrsinaPolja = Double.parseDouble(povrsinaPoljaTextField.getText());
		Connection veza = null;
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li sigurno promjeniti artikl?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA) {
		try {
			veza = BazaPodataka.connectToDatabase();

			PreparedStatement updateTable = veza
					.prepareStatement("UPDATE RAZVOJ.POLJE SET IMEPOLJA = ?, LOKACIJAPOLJA = ?, POVRSINAPOLJA = ? WHERE POLJEID = ?");
			updateTable.setString(1,
					imePolja);
			updateTable.setString(2,
					lokacijaPolja);
			updateTable.setDouble(3,
					povrsinaPolja);
			updateTable.setInt(4,
					polje.getPoljeID());
			updateTable.execute();

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
		
		}else{
			//ne mijenja se
		}
		initialize();
	}
	
	public void obrisiOdabranoButtonClicked() {
		System.out.println("obrisiOdabrano clicked");
		
		Polje polje = poljaTable.getSelectionModel().getSelectedItem();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li sigurno obrisati artikl?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA) {

			Connection veza = null;

			try {
				veza = BazaPodataka.connectToDatabase();
				PreparedStatement obrisiArtikl = veza
						.prepareStatement("DELETE FROM RAZVOJ.PODACIPOLJA WHERE POLJEID = ?");
				obrisiArtikl.setInt(1, polje.getPoljeID());
				obrisiArtikl.execute();
				PreparedStatement obrisiPolje = veza
						.prepareStatement("DELETE FROM RAZVOJ.POLJE WHERE POLJEID = ? AND IMEPOLJA = ?");
				obrisiPolje.setInt(1, polje.getPoljeID());
				obrisiPolje.setString(2, polje.getImePolja());
				obrisiPolje.execute();
				polje.setPoljeID(null);
				polje.setGodinaID(null);
				polje.setImePolja(null);
				polje.setLokacijaPolja(null);
				polje.setPovrsinaPolja(null);
				polje.setPrinos(null);
				polje.setCijenaOtkupa(null);
				polje.setInfo(null);
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

		} else {
			// ne briše se
		}
		initialize();
	}
	
	public void natragButtonClicked() {
		System.out.println("natragButton clicked");

		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/Izbornik.fxml"));
			Scene scene = new Scene(root);
			Stage stage = Glavna.window;
			stage.close();
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void poljaTableClicked() {
		System.out.println("poljaTable clicked");
		Polje polje = poljaTable.getSelectionModel().getSelectedItem();
		
		imePoljaTextField.setText(polje.getImePolja());
		lokacijaPoljaTextField.setText(polje.getLokacijaPolja());
		povrsinaPoljaTextField.setText(polje.getPovrsinaPolja().toString());
		
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li uæi u polje " + polje.getImePolja() +"?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA){
			Integer poljeID = null;
			Integer godinaID = null;
			String imePolja = null;
			String lokacijaPolja = null;
			Double povrsinaPolja = null;
			Double prinos = null;
			Double cijenaOtkupa = null;
			Double potpora = null;
			String info = null;
			
			poljeSelected = new Polje (poljeID, godinaID, imePolja, lokacijaPolja, povrsinaPolja, prinos, cijenaOtkupa, potpora, info);
			poljeSelected = polje;
			System.out.println(poljeSelected.getImePolja());
			System.out.println(poljeSelected.getLokacijaPolja());
			System.out.println(poljeSelected.getPovrsinaPolja());
			
			try{
				Parent root = FXMLLoader.load(getClass().getResource("../fxml/DetaljiPolja.fxml"));
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
