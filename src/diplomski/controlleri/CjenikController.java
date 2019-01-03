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
import diplomski.entiteti.Cjenik;
import diplomski.entiteti.TipArtikla;
import diplomski.glavna.Glavna;
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
import javafx.util.StringConverter;

public class CjenikController {

	@FXML
	public Label korisnikLabel;

	@FXML
	public Label godinaLabel;

	@FXML
	public TextField imeArtiklaTextField;

	@FXML
	public TextField cijenaArtiklaTextField;

	@FXML
	public TextField datumCijeneTextField;

	@FXML
	private TableView<Cjenik> cjenikTable;

	@FXML
	private TableColumn<Cjenik, String> imeArtiklaColumn;

	@FXML
	private TableColumn<Cjenik, Double> cijenaColumn;

	@FXML
	private TableColumn<Cjenik, String> datumCijeneColumn;

	@FXML
	private ChoiceBox<TipArtikla> tipArtiklaChoiceBox;

	List<TipArtikla> listaArtikala;
	@FXML
	public void initialize() {
		Integer artiklID = null;
		Integer godinaID = null;
		Integer tipartiklaID = null;
		String imeArtikla = null;
		Double cijenaArtikla = null;
		String datumCijene = null;
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		godinaLabel.setText(GodineController.godinaSelected.getGodina()
				.toString());

		imeArtiklaColumn
				.setCellValueFactory(new PropertyValueFactory<Cjenik, String>(
						"imeArtikla"));

		cijenaColumn
				.setCellValueFactory(new PropertyValueFactory<Cjenik, Double>(
						"cijenaArtikla"));

		datumCijeneColumn
				.setCellValueFactory(new PropertyValueFactory<Cjenik, String>(
						"datumCijeneColumn"));

		tipArtiklaChoiceBox.setStyle("-fx-font-size: 14;");
		Connection veza = null;
		List<Cjenik> listaCjenik = new ArrayList<>();

		try {
			veza = BazaPodataka.connectToDatabase();
			listaArtikala = new ArrayList<>();

			if (tipArtiklaChoiceBox.getItems().isEmpty()) {

				Integer tipArtiklaID = null;
				String tipArtikla = null;
				String proizvodac = null;
				Integer godinaNabave = null;
				String dodatniInfo = null;
				PreparedStatement popuniTablicu = veza
						.prepareStatement("SELECT * FROM RAZVOJ.TIPARTIKLA ORDER BY TIPARTIKLA");
				ResultSet rs = popuniTablicu.executeQuery();
				while (rs.next()) {
					TipArtikla artikli = new TipArtikla(tipArtiklaID,
							tipArtikla, proizvodac, godinaNabave, dodatniInfo);
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
					tipArtiklaChoiceBox.getItems().add(artikli);
					tipArtiklaChoiceBox
							.setConverter(new StringConverter<TipArtikla>() {
								@Override
								public String toString(TipArtikla tipArtikla) {

									return tipArtikla.getTipArtikla() + " ("
											+ tipArtikla.getProizvodac() + ", "
											+ tipArtikla.getGodinaNabave()
											+ ")";

								}

								@SuppressWarnings("null")
								@Override
								public TipArtikla fromString(String s) {
									TipArtikla artikl = null;
									artikl.setTipArtikla(s);
									return artikl;

								}
							});
				}
			}

			tipArtiklaChoiceBox.getSelectionModel().select(0);
			PreparedStatement popuniTablicu = veza
					.prepareStatement("SELECT * FROM RAZVOJ.CJENIK  WHERE GODINAID = ?");
			popuniTablicu.setInt(1,
					GodineController.godinaSelected.getGodinaID());
			ResultSet rs = popuniTablicu.executeQuery();
			while (rs.next()) {
				Cjenik cjenik = new Cjenik(artiklID, godinaID, tipartiklaID,
						imeArtikla, cijenaArtikla, datumCijene);
				artiklID = rs.getInt("ARTIKLID");
				godinaID = rs.getInt("GODINAID");
				tipartiklaID = rs.getInt("TIPARTIKLAID");
				imeArtikla = rs.getString("IMEARTIKLA");
				cijenaArtikla = rs.getDouble("CIJENAARTIKLA");
				datumCijene = rs.getString("DATUMCIJENE");
				cjenik.setArtiklID(artiklID);
				cjenik.setGodinaID(godinaID);
				cjenik.setTipartiklaID(tipartiklaID);
				cjenik.setImeArtikla(imeArtikla);
				cjenik.setCijenaArtikla(cijenaArtikla);
				cjenik.setDatumCijene(datumCijene);
				listaCjenik.add(cjenik);
			}

			ObservableList<Cjenik> lista = FXCollections
					.observableArrayList(listaCjenik);
			imeArtiklaColumn.setCellValueFactory(new PropertyValueFactory<>(
					"imeArtikla"));
			imeArtiklaColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			cijenaColumn.setCellValueFactory(new PropertyValueFactory<>(
					"cijenaArtikla"));
			cijenaColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			datumCijeneColumn.setCellValueFactory(new PropertyValueFactory<>(
					"datumCijene"));
			datumCijeneColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			cjenikTable.setItems(lista);

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
				+ GodineController.godinaSelected.getGodina()
				+ "\nInfo o godini: "
				+ GodineController.godinaSelected.getInfoGodina());

		alert.showAndWait();
	}

	public void obrisiOdabranoButtonClicked() {
		System.out.println("obrisiOdabranoButton clicked");
		Cjenik cjenik = cjenikTable.getSelectionModel().getSelectedItem();
		
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
						.prepareStatement("DELETE FROM RAZVOJ.CJENIK WHERE ARTIKLID = ? AND IMEARTIKLA = ?");
				obrisiArtikl.setInt(1, cjenik.getArtiklID());
				obrisiArtikl.setString(2, cjenik.getImeArtikla());
				obrisiArtikl.execute();
				cjenik.setArtiklID(null);
				cjenik.setGodinaID(null);
				cjenik.setTipartiklaID(null);
				cjenik.setImeArtikla(null);
				cjenik.setCijenaArtikla(null);
				cjenik.setDatumCijene(null);
				
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

	public void promjeniOdabranoButtonClicked() {
		System.out.println("promjeniOdabranoButton clicked");
		
		
		Cjenik cjenik = cjenikTable.getSelectionModel().getSelectedItem();
		
		//Integer godinaID = GodineController.godinaSelected.getGodinaID();
		Integer tipartiklaID = tipArtiklaChoiceBox.getValue()
				.getTipArtiklaID();
		String imeArtikla = imeArtiklaTextField.getText();			
		Double cijenaArtikla = Double.parseDouble(cijenaArtiklaTextField.getText());
		String datumCijene = datumCijeneTextField.getText();
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
					.prepareStatement("UPDATE RAZVOJ.CJENIK SET TIPARTIKLAID = ?, IMEARTIKLA = ?, CIJENAARTIKLA = ?, DATUMCIJENE = ? WHERE ARTIKLID = ? AND GODINAID = ?");
			updateTable.setInt(1,
					tipartiklaID);
			updateTable.setString(2,
					imeArtikla);
			updateTable.setDouble(3,
					cijenaArtikla);
			updateTable.setString(4,
					datumCijene);
			updateTable.setInt(5,
					cjenik.getArtiklID());
			updateTable.setInt(6,
					cjenik.getGodinaID());
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

	public void spremiButtonClicked() {
		System.out.println("spremiButton clicked");

		Connection veza = null;

		try {

			Integer godinaID = GodineController.godinaSelected.getGodinaID();
			Integer tipartiklaID = tipArtiklaChoiceBox.getValue()
					.getTipArtiklaID();
			String imeArtikla = imeArtiklaTextField.getText();			
			Double cijenaArtikla = Double.parseDouble(cijenaArtiklaTextField.getText());
			String datumCijene = datumCijeneTextField.getText();

			veza = BazaPodataka.connectToDatabase();

			PreparedStatement preparedStatement = veza
					.prepareStatement("INSERT INTO RAZVOJ.CJENIK "
							+ "(GODINAID, TIPARTIKLAID, IMEARTIKLA, CIJENAARTIKLA, DATUMCIJENE) VALUES (?, ?, ?, ?, ?)");
			preparedStatement.setInt(1, godinaID);
			preparedStatement.setInt(2, tipartiklaID);
			preparedStatement.setString(3, imeArtikla);
			preparedStatement.setDouble(4, cijenaArtikla);
			preparedStatement.setString(5, datumCijene);
			preparedStatement.execute();

			imeArtiklaTextField.setText(null);
			cijenaArtiklaTextField.setText(null);
			datumCijeneTextField.setText(null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Unesite broj! Ukoliko je decimalan koristite toèku umjesto zareza");

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

	public void artikliButtonClicked() {
		System.out.println("artikliButton clicked");

		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/Artikli.fxml"));
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

	public void helpLabelClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Cijene su predviðene u kunama.\n"
				+ "U sluèaju gnojiva: kn/kg\n"
				+ "U sluèaju špriceva: kn/l\n" + "U sluèaju goriva: kn/l\n"
				+ "U ostalim sluèajevima: kn/ha");

		alert.showAndWait();
	}
	
	public void cjenikTableClicked(){
		Cjenik cjenik = cjenikTable.getSelectionModel().getSelectedItem();
		
		imeArtiklaTextField.setText(cjenik.getImeArtikla());
		cijenaArtiklaTextField.setText(cjenik.getCijenaArtikla().toString());
		datumCijeneTextField.setText(cjenik.getDatumCijene());
		

		Integer tipArtiklaID = null;
		String tipArtikla = null;
		String proizvodac = null;
		Integer godinaNabave = null;
		String dodatniInfo = null;
		Connection veza = null;
		
		try {
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement povuciArtikl = veza
					.prepareStatement("SELECT * FROM RAZVOJ.TIPARTIKLA WHERE TIPARTIKLAID = ?");
			povuciArtikl.setInt(1, cjenik.getTipartiklaID());
			ResultSet rs = povuciArtikl.executeQuery();
			while (rs.next()) {
				
				tipArtiklaID = rs.getInt("TIPARTIKLAID");
				tipArtikla = rs.getString("TIPARTIKLA");
				proizvodac = rs.getString("PROIZVODAC");
				godinaNabave = rs.getInt("GODINANABAVE");
				dodatniInfo = rs.getString("DODATNIINFO");
			}
			TipArtikla artikli = new TipArtikla(tipArtiklaID, tipArtikla, proizvodac, godinaNabave, dodatniInfo);
		
			tipArtiklaChoiceBox.getSelectionModel().select(artikli);
		
		}
		
		
		catch (IOException e) {
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
}
