package diplomski.controlleri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import diplomski.database.BazaPodataka;
import diplomski.database.Enkripcija;
import diplomski.entiteti.Korisnik;
import diplomski.glavna.Glavna;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UlazController {

	@FXML
	public Button izlaz;

	@FXML
	public TextField korisnickoImeTextField;

	@FXML
	public TextField lozinkaTextField;

	public static Korisnik korisnik;

	public void izlazButtonClicked() {

		System.out.println("izlazButton clicked");

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li sigurno izaæi iz programa?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA) {
			Stage stage = Glavna.window;
			stage.close();
		} else {
			// izlaz iz programa
		}

	}

	public void ulazButtonClicked() {

		System.out.println("ulazButton clicked");

		Integer korisnikID = null;
		String korisnickoIme = null;
		String lozinka = null;
		String ime = null;
		String prezime = null;
		String opg = null;
		Connection veza = null;

		String passhash = lozinkaTextField.getText();

		System.out.println("1: " + Enkripcija.MD5(passhash));

		try {
			veza = BazaPodataka.connectToDatabase();

			PreparedStatement traziKorisnika = veza
					.prepareStatement("SELECT * FROM RAZVOJ.KORISNIK WHERE KORISNICKOIME = ? AND LOZINKA = ?");
			traziKorisnika.setString(1, korisnickoImeTextField.getText());
			traziKorisnika.setString(2,
					Enkripcija.MD5(lozinkaTextField.getText()));
			ResultSet rs = traziKorisnika.executeQuery();
			while (rs.next()) {
				korisnikID = rs.getInt("KORISNIKID");
				korisnickoIme = rs.getString("KORISNICKOIME");
				lozinka = rs.getString("LOZINKA");
				ime = rs.getString("IME");
				prezime = rs.getString("PREZIME");
				opg = rs.getString("OPG");
			}
			korisnik = new Korisnik(korisnikID, korisnickoIme, lozinka, ime,
					prezime, opg);
			korisnik.setKorisnikID(korisnikID);
			korisnik.setKorisnickoIme(korisnickoIme);
			korisnik.setLozinka(lozinka);
			korisnik.setIme(ime);
			korisnik.setPrezime(prezime);
			korisnik.setOpg(opg);

			if (korisnikID == null) {
				System.out.println("Netoèno korisnièko ime ili lozinka!");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText("Netoèno korisnièko ime ili lozinka!");
				alert.setContentText("Pokušajte ponovo...");

				alert.showAndWait();
			} else {
				System.out.println("Korisnik potvrðen:");
				System.out.println(korisnik.getKorisnikID());
				System.out.println(korisnik.getKorisnickoIme());

				try {
					Parent root = FXMLLoader.load(getClass().getResource(
							"../fxml/Godine.fxml"));
					Scene scene = new Scene(root);
					Stage stage = Glavna.window;
					stage.hide();
					stage.setScene(scene);
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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

	public void kreirajButtonClicked() {

		System.out.println("kreirajButton clicked");
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/KreirajKorisnika.fxml"));
			Scene scene = new Scene(root);
			Stage stage = Glavna.window;
			stage.hide();
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
