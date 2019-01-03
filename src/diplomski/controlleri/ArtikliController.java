package diplomski.controlleri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import diplomski.database.BazaPodataka;
import diplomski.entiteti.TipArtikla;
import diplomski.glavna.Glavna;

public class ArtikliController {

	@FXML
	public Label korisnikLabel;

	@FXML
	public Label godinaLabel;
	
	@FXML
	public TextField proizvodacTextField;
	
	@FXML
	public TextField godinaNabaveTextField;
	
	@FXML
	public TextField dodatniInfoTextField;

	@FXML
	private TableView<TipArtikla> artikliTable;
	
	@FXML
	private TableColumn<TipArtikla, String> tipArtiklaColumn;
	
	@FXML
	private TableColumn<TipArtikla, String> proizvodacColumn;
	
	@FXML
	private TableColumn<TipArtikla, Integer> godinaNabaveColumn;

	@FXML
	private ChoiceBox<String> tipArtiklaChoiceBox;
	
	@FXML
	public void initialize() {
		
		Integer tipArtiklaID = null;
		String tipArtikla = null;
		String proizvodac = null;
		Integer godinaNabave = null;
		String dodatniInfo = null;
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		godinaLabel.setText(GodineController.godinaSelected.getGodina()
				.toString());
		if(tipArtiklaChoiceBox.getItems().isEmpty()){
			tipArtiklaChoiceBox.getItems().addAll("Sjeme", "Gnojivo", "Pesticid", "Herbicid", "Fungicid", "Prihrana", "Gorivo", "Ostalo" );
			tipArtiklaChoiceBox.setValue("Sjeme");	
			tipArtiklaChoiceBox.setStyle("-fx-font-size: 14;");
		}
		
		tipArtiklaColumn.setCellValueFactory(new PropertyValueFactory<TipArtikla, String>("tipArtikla"));
		
		proizvodacColumn.setCellValueFactory(new PropertyValueFactory<TipArtikla, String>("proizvodac"));
		
		godinaNabaveColumn.setCellValueFactory(new PropertyValueFactory<TipArtikla, Integer>("godinaNabave"));
		
		Connection veza = null;
		List<TipArtikla> listaArtikala = new ArrayList<>();
		try {
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement popuniTablicu = veza
					.prepareStatement("SELECT * FROM RAZVOJ.TIPARTIKLA ORDER BY TIPARTIKLA");
			ResultSet rs = popuniTablicu.executeQuery();
			while (rs.next()) {
				TipArtikla artikli = new TipArtikla(tipArtiklaID, tipArtikla, proizvodac, godinaNabave, dodatniInfo);
				tipArtiklaID = rs.getInt("TIPARTIKLAID");
				tipArtikla = rs.getString("TIPARTIKLA");
				proizvodac = rs.getString("PROIZVODAC");
				godinaNabave = rs.getInt("GODINANABAVE");
				dodatniInfo = rs.getString("DODATNIINFO");
				artikli.setTipArtiklaID(tipArtiklaID);
				artikli.setTipArtikla(tipArtikla);
				artikli.setProizvodac(proizvodac);
				artikli.setGodinaNabave(godinaNabave);
				artikli.setDodatniInfo(dodatniInfo);
				listaArtikala.add(artikli);
			}
			
			ObservableList<TipArtikla> lista = FXCollections.observableArrayList(listaArtikala);
			tipArtiklaColumn.setCellValueFactory(new PropertyValueFactory<>("tipArtikla"));
			tipArtiklaColumn.setStyle( "-fx-alignment: CENTER;\n"+ "-fx-font-size: 16;");
			proizvodacColumn.setCellValueFactory(new PropertyValueFactory<>("proizvodac"));
			proizvodacColumn.setStyle( "-fx-alignment: CENTER;\n"+ "-fx-font-size: 16;");
			godinaNabaveColumn.setCellValueFactory(new PropertyValueFactory<>("godinaNabave"));
			godinaNabaveColumn.setStyle( "-fx-alignment: CENTER;\n"+ "-fx-font-size: 16;");
			artikliTable.setItems(lista);
			
			
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
	
	public void godinaLabelClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Godina: "
				+ GodineController.godinaSelected.getGodina() + "\nInfo o godini: "
				+ GodineController.godinaSelected.getInfoGodina());

		alert.showAndWait();
	}
	
	public void obrisiOdabranoButtonClicked(){
		System.out.println("obrisiOdabranoButton clicked");
		
		TipArtikla artikl = artikliTable.getSelectionModel().getSelectedItem();
		
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
				PreparedStatement obrisiCjenik = veza
						.prepareStatement("DELETE FROM RAZVOJ.CJENIK WHERE TIPARTIKLAID = ?");
				obrisiCjenik.setInt(1, artikl.getTipArtiklaID());
				obrisiCjenik.execute();
				PreparedStatement obrisiArtikl = veza
						.prepareStatement("DELETE FROM RAZVOJ.TIPARTIKLA WHERE TIPARTIKLAID = ? AND TIPARTIKLA = ?");
				obrisiArtikl.setInt(1, artikl.getTipArtiklaID());
				obrisiArtikl.setString(2, artikl.getTipArtikla());
				obrisiArtikl.execute();
				artikl.setTipArtiklaID(null);
				artikl.setTipArtikla(null);
				artikl.setProizvodac(null);
				artikl.setGodinaNabave(null);
				artikl.setDodatniInfo(null);
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
	
	public void promjeniOdabranoButtonClicked(){
		System.out.println("promjeniOdabranoButton clicked");
		
		TipArtikla artikl = artikliTable.getSelectionModel().getSelectedItem();
		String tipArtikla = tipArtiklaChoiceBox.getValue();
		String proizvodac = proizvodacTextField.getText();
		Integer godinaNabave = Integer.parseInt(godinaNabaveTextField.getText());
		String dodatniInfo = dodatniInfoTextField.getText();
		
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
					.prepareStatement("UPDATE RAZVOJ.TIPARTIKLA SET TIPARTIKLA = ?, PROIZVODAC = ?, GODINANABAVE = ?, DODATNIINFO = ? WHERE TIPARTIKLAID = ?");
			updateTable.setString(1,
					tipArtikla);
			updateTable.setString(2,
					proizvodac);
			updateTable.setInt(3,
					godinaNabave);
			updateTable.setString(4,
					dodatniInfo);
			updateTable.setInt(5,
					artikl.getTipArtiklaID());
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

	public void spremiButtonClicked(){
		System.out.println("spremiButton clicked");
		
		Connection veza = null;
		
		try{
			String tipArtikla = tipArtiklaChoiceBox.getValue();
			String proizvodac = proizvodacTextField.getText();
			Integer godinaNabave = Integer.parseInt(godinaNabaveTextField.getText());
			String dodatniInfo = dodatniInfoTextField.getText();
			
			veza = BazaPodataka.connectToDatabase();
			if (Integer.toString(godinaNabave).length() == 4 && 1950 < godinaNabave && godinaNabave < 2016) {
		PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO RAZVOJ.TIPARTIKLA "
				+ "(TIPARTIKLA, PROIZVODAC, GODINANABAVE, DODATNIINFO) VALUES (?, ?, ?, ?)");
		preparedStatement.setString(1, tipArtikla);
		preparedStatement.setString(2, proizvodac);
		preparedStatement.setInt(3, godinaNabave);
		preparedStatement.setString(4, dodatniInfo);
		preparedStatement.execute();
		
		proizvodacTextField.setText(null);
		godinaNabaveTextField.setText(null);
		dodatniInfoTextField.setText(null);
			} if (Integer.toString(godinaNabave).length() == 4 && godinaNabave <= 1950) {
				System.out
				.println("Unesite godinu veæu od 1950!");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText(null);
				alert.setContentText("Unesite godinu veæu od 1950!");
				alert.showAndWait();
			}
			if (Integer.toString(godinaNabave).length() == 4 && 2016 <= godinaNabave) {
				System.out
				.println("Unesite godinu manju od 2016!");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText(null);
				alert.setContentText("Unesite godinu manju od 2016!");
				alert.showAndWait();
			}
				
			if (Integer.toString(godinaNabave).length() != 4){
				System.out
						.println("Godina je èetveroznamenkasti cijeli broj!");
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Godina je èetveroznamenkasti cijeli broj!");

			alert.showAndWait();
			}
		} catch(IOException e) {
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

	public void natragButtonClicked(){
		System.out.println("natragButton clicked");
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/Cjenik.fxml"));
			Scene scene = new Scene(root);
			Stage stage = Glavna.window;
			stage.close();
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
			// natrag
		}
	}
	
	public void artikliTableClicked() {
		TipArtikla artikl = artikliTable.getSelectionModel().getSelectedItem();
		
		tipArtiklaChoiceBox.setValue(artikl.getTipArtikla());
		proizvodacTextField.setText(artikl.getProizvodac());
		godinaNabaveTextField.setText(artikl.getGodinaNabave().toString());
		dodatniInfoTextField.setText(artikl.getDodatniInfo());
	}
}
