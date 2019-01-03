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
import diplomski.entiteti.PodaciPolja;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class DetaljiPoljaController {
	
	@FXML
	public Label korisnikLabel;

	@FXML
	public Label godinaLabel;
	
	@FXML
	public Label imePoljaLabel;
	
	@FXML
	public Label lokacijaPoljaLabel;
	
	@FXML
	public Label povrsinaPoljaLabel;
	
	@FXML
	public Label prihodLabel;
	
	@FXML
	public Label ukupniTrosakLabel;
	
	@FXML
	public Label profitLabel;
	
	@FXML
	public TextField kolicinaTextField;
	
	@FXML
	public TextField prinosTextField;
	
	@FXML
	public TextField cijenaOtkupaTextField;
	
	@FXML
	public TextField potporaTextField;
	
	@FXML
	public TextField infoTextField;
	
	@FXML
	private TableView<PodaciPolja> detaljiPoljaTable;
	
	@FXML
	private TableColumn<PodaciPolja, String> artiklColumn;
	
	@FXML
	private TableColumn<PodaciPolja, Double> cijenaColumn;
	
	@FXML
	private TableColumn<PodaciPolja, Double> kolicinaColumn;
	
	@FXML
	private TableColumn<PodaciPolja, Double> trosakColumn;
	
	@FXML
	public ChoiceBox<Cjenik> artiklChoiceBox;
	
	List<Cjenik> listaArtikala;
	
	Double ukupniTroskovi;
	
	@FXML
	public void initialize() {
		Double ukupniPrihodi = 0.0;
		
		ukupniTroskovi = 0.0;
		Double profit = 0.0;
		Integer podaciPoljaID = null;
		Integer poljeID = null;
		String tipArtikla = null;
		Double cijenaArtikla = null;
		Double kolicina = null;
		Double trosak = null;
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		godinaLabel.setText(GodineController.godinaSelected.getGodina()
				.toString());
		imePoljaLabel.setText("Ime polja: " + IzborPoljaController.poljeSelected.getImePolja());
		lokacijaPoljaLabel.setText("Lokacija: " + IzborPoljaController.poljeSelected.getLokacijaPolja());
		povrsinaPoljaLabel.setText("Površina: " + IzborPoljaController.poljeSelected.getPovrsinaPolja().toString() + " ha");
		
		artiklColumn.setCellValueFactory(new PropertyValueFactory<PodaciPolja, String>(
				"tipArtikla"));
		cijenaColumn.setCellValueFactory(new PropertyValueFactory<PodaciPolja, Double>(
				"cijenaArtikla"));
		kolicinaColumn.setCellValueFactory(new PropertyValueFactory<PodaciPolja, Double>(
				"kolicina"));
		trosakColumn.setCellValueFactory(new PropertyValueFactory<PodaciPolja, Double>(
				"trosak"));
		artiklChoiceBox.setStyle("-fx-font-size: 14;");
		Connection veza = null;
		try {
			
			
			veza = BazaPodataka.connectToDatabase();
			listaArtikala = new ArrayList<>();

			if (artiklChoiceBox.getItems().isEmpty()) {

				Integer artiklID = null;
				Integer godinaID = null;
				Integer tipartiklaID = null;
				String imeArtikla = null;
				cijenaArtikla = null;
				String datumCijene = null;
				PreparedStatement popuni = veza
						.prepareStatement("SELECT * FROM RAZVOJ.CJENIK WHERE GODINAID = ?");
				popuni.setInt(1, GodineController.godinaSelected.getGodinaID());
				ResultSet rs = popuni.executeQuery();
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

					listaArtikala.add(cjenik);
					artiklChoiceBox.getItems().add(cjenik);
					artiklChoiceBox
							.setConverter(new StringConverter<Cjenik>() {
								@Override
								public String toString(Cjenik cjenik) {

									return cjenik.getImeArtikla() + ", "
											+ cjenik.getCijenaArtikla() + "kn ("
											+ cjenik.getDatumCijene()
											+ ")";

								}

								@SuppressWarnings("null")
								@Override
								public Cjenik fromString(String s) {
									Cjenik cjenik = null;
									cjenik.setImeArtikla(s);
									return cjenik;

								}
							});
				}
			}
			
			List<PodaciPolja> podaciPoljaList = new ArrayList<>();
			PreparedStatement popuniTablicu = veza
					.prepareStatement("SELECT * FROM RAZVOJ.PODACIPOLJA  WHERE POLJEID = ?");
			popuniTablicu.setInt(1,
					IzborPoljaController.poljeSelected.getPoljeID());
			ResultSet rs = popuniTablicu.executeQuery();
			while (rs.next()) {
				PodaciPolja podaciPolja = new PodaciPolja(podaciPoljaID, poljeID, tipArtikla, cijenaArtikla, kolicina, trosak);
				podaciPoljaID = rs.getInt("PODACIPOLJAID");
				poljeID = rs.getInt("POLJEID");
				tipArtikla = rs.getString("TIPARTIKLA");
				cijenaArtikla = rs.getDouble("CIJENAARTIKLA");
				kolicina = rs.getDouble("KOLICINA");
				trosak = cijenaArtikla * kolicina;
				podaciPolja.setPodaciPoljaID(podaciPoljaID);
				podaciPolja.setPoljeID(poljeID);
				podaciPolja.setTipArtikla(tipArtikla);
				podaciPolja.setCijenaArtikla(cijenaArtikla);
				podaciPolja.setKolicina(kolicina);
				podaciPolja.setTrosak(trosak);
				ukupniTroskovi = ukupniTroskovi + trosak;
				podaciPoljaList.add(podaciPolja);
			}
			
			
			ObservableList<PodaciPolja> lista = FXCollections
					.observableArrayList(podaciPoljaList);
			artiklColumn.setCellValueFactory(new PropertyValueFactory<>(
					"tipArtikla"));
			artiklColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			cijenaColumn.setCellValueFactory(new PropertyValueFactory<>(
					"cijenaArtikla"));
			cijenaColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			kolicinaColumn.setCellValueFactory(new PropertyValueFactory<>(
					"kolicina"));
			kolicinaColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			trosakColumn.setCellValueFactory(new PropertyValueFactory<>(
					"trosak"));
			trosakColumn.setStyle("-fx-alignment: CENTER;\n"
					+ "-fx-font-size: 16;");
			detaljiPoljaTable.setItems(lista);

			artiklChoiceBox.getSelectionModel().select(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				BazaPodataka.closeConnectionToDatabase(veza);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		if (IzborPoljaController.poljeSelected.getPrinos() != null && IzborPoljaController.poljeSelected.getCijenaOtkupa() != null){
		ukupniPrihodi = IzborPoljaController.poljeSelected.getPrinos() * IzborPoljaController.poljeSelected.getCijenaOtkupa();
		}
		if (IzborPoljaController.poljeSelected.getPotpora() != null){
		ukupniPrihodi = ukupniPrihodi + IzborPoljaController.poljeSelected.getPotpora();
		}
		profit = ukupniPrihodi - ukupniTroskovi;
		if (IzborPoljaController.poljeSelected.getPrinos() != null){
		prinosTextField.setText(IzborPoljaController.poljeSelected.getPrinos().toString());
		}
		if (IzborPoljaController.poljeSelected.getCijenaOtkupa() != null){
		cijenaOtkupaTextField.setText(IzborPoljaController.poljeSelected.getCijenaOtkupa().toString());
		}
		if (IzborPoljaController.poljeSelected.getPotpora() != null){
		potporaTextField.setText(IzborPoljaController.poljeSelected.getPotpora().toString());
		}
		if (IzborPoljaController.poljeSelected.getInfo() != null){
		infoTextField.setText(IzborPoljaController.poljeSelected.getInfo());
		}
		
		prihodLabel.setText(ukupniPrihodi.toString() + " kn");
		ukupniTrosakLabel.setText(ukupniTroskovi.toString() + " kn");
		profitLabel.setText(profit.toString() + " kn");
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
	
	public void dodajNovoButtonClicked() {
		
		Connection veza = null;
		try{
			Double kolicina = Double.parseDouble(kolicinaTextField.getText());
			if (kolicina.equals("")) {
				System.out.println("Korisnik treba obavezno unijeti kolièinu!! ");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText("Korisnik treba obavezno unijeti kolièinu! ");
				alert.setContentText("Provjerite jeste li unijeli kolièinu.");

				alert.showAndWait();
			}else{
				veza = BazaPodataka.connectToDatabase();
				
				PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO RAZVOJ.PODACIPOLJA "
						+ "(POLJEID, TIPARTIKLA, CIJENAARTIKLA, KOLICINA) VALUES (?, ?, ?, ?)");
				preparedStatement.setInt(1, IzborPoljaController.poljeSelected.getPoljeID());
				preparedStatement.setString(2, artiklChoiceBox.getValue().getImeArtikla());
				preparedStatement.setDouble(3, artiklChoiceBox.getValue().getCijenaArtikla());
				preparedStatement.setDouble(4, kolicina);
				preparedStatement.execute();
			}	
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText(null);
			alert.setContentText("Unesite broj kolièinu! Ukoliko je decimalan koristite toèku umjesto zareza.");

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
	
	public void promjeniButtonClicked() {
PodaciPolja podaciPolja = detaljiPoljaTable.getSelectionModel().getSelectedItem();
	
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
			Double kolicina = Double.parseDouble(kolicinaTextField.getText());
			PreparedStatement updateTable = veza
					.prepareStatement("UPDATE RAZVOJ.PODACIPOLJA SET TIPARTIKLA = ?, CIJENAARTIKLA = ?, KOLICINA = ? WHERE PODACIPOLJAID = ? AND POLJEID = ?");
			updateTable.setString(1,
					artiklChoiceBox.getValue().getImeArtikla());
			updateTable.setDouble(2,
					artiklChoiceBox.getValue().getCijenaArtikla());
			updateTable.setDouble(3,
					kolicina);
			updateTable.setInt(4,
					podaciPolja.getPodaciPoljaID());
			updateTable.setInt(5,
					podaciPolja.getPoljeID());
			updateTable.execute();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Evidencija Polja");
			alert2.setHeaderText(null);
			alert2.setContentText("Unesite broj kolièinu! Ukoliko je decimalan koristite toèku umjesto zareza.");

			alert2.showAndWait();
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
	
	public void obrisiButtonClicked() {
		PodaciPolja podaciPolja = detaljiPoljaTable.getSelectionModel().getSelectedItem();
		
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
						.prepareStatement("DELETE FROM RAZVOJ.PODACIPOLJA WHERE PODACIPOLJAID = ? AND TIPARTIKLA = ?");
				obrisiArtikl.setInt(1, podaciPolja.getPodaciPoljaID());
				obrisiArtikl.setString(2, podaciPolja.getTipArtikla());
				obrisiArtikl.execute();
				podaciPolja.setPodaciPoljaID(null);
				podaciPolja.setPoljeID(null);
				podaciPolja.setTipArtikla(null);
				podaciPolja.setCijenaArtikla(null);
				podaciPolja.setKolicina(null);
				podaciPolja.setTrosak(null);
				
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
	
	public void detaljiPoljaTableClicked() {
		PodaciPolja podaciPolja = detaljiPoljaTable.getSelectionModel().getSelectedItem();
		
		kolicinaTextField.setText(podaciPolja.getKolicina().toString());
		//artiklChoiceBox.setValue(podaciPolja);
	}
	
	public void helpClicked() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Kolièine su predviðene...\n"
				+ "U sluèaju gnojiva: kg\n"
				+ "U sluèaju špriceva: l\n" + "U sluèaju goriva: l\n"
				+ "U ostalim sluèajevima: ha");

		alert.showAndWait();
	}
	
	public void natragButtonClicked() {
		System.out.println("natragButton clicked");

		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/IzborPolja.fxml"));
			Scene scene = new Scene(root);
			Stage stage = Glavna.window;
			stage.close();
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void spremiButtonClicked() {
		Connection veza = null;
		try{
			Double ukupniPrihodi = 0.0;
			
			Double profit = 0.0;
			Double prinos = Double.parseDouble(prinosTextField.getText());
			Double cijenaOtkupa = Double.parseDouble(cijenaOtkupaTextField.getText());
			Double potpora = Double.parseDouble(potporaTextField.getText());
			String info = infoTextField.getText();
			
			ukupniPrihodi = prinos * cijenaOtkupa;
			ukupniPrihodi = ukupniPrihodi + potpora;
			
			profit = ukupniPrihodi - ukupniTroskovi;
			veza = BazaPodataka.connectToDatabase();
			PreparedStatement update = veza
					.prepareStatement("UPDATE RAZVOJ.POLJE SET PRINOS = ?, CIJENAOTKUPA = ?, INFO = ?, POTPORA = ? WHERE POLJEID = ? AND GODINAID = ?");
			update.setDouble(1,
					prinos);
			update.setDouble(2,
					cijenaOtkupa);
			update.setString(3,
					info);
			update.setDouble(4,
					potpora);
			update.setInt(5,
					IzborPoljaController.poljeSelected.getPoljeID());
			update.setInt(6,
					GodineController.godinaSelected.getGodinaID());
			update.execute();
			
			prihodLabel.setText(ukupniPrihodi.toString() + " kn");
			ukupniTrosakLabel.setText(ukupniTroskovi.toString() + " kn");
			profitLabel.setText(profit.toString() + " kn");
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText(null);
				alert.setContentText("Unesite broj prinos, cijenu otkupa i potporu! Ukoliko je decimalan koristite toèku umjesto zareza.");

				alert.showAndWait();
			}

			finally{
				try{
						BazaPodataka.closeConnectionToDatabase(veza);
				} catch(SQLException ex) {
				ex.printStackTrace();
				 }
				}
		
	}
}
