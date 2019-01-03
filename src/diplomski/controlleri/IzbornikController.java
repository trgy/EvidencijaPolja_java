package diplomski.controlleri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import diplomski.database.BazaPodataka;
import diplomski.glavna.Glavna;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Stage;

public class IzbornikController {

	@FXML
	public Label korisnikLabel;

	@FXML
	public Label godinaLabel;

	@FXML
	public TextArea GodinaInfoTextArea;

	@FXML
	public void initialize() {
		korisnikLabel.setText(UlazController.korisnik.getKorisnickoIme());
		godinaLabel.setText(GodineController.godinaSelected.getGodina()
				.toString());
		GodinaInfoTextArea.setText(GodineController.godinaSelected.getInfoGodina());
	
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

	public void natragButtonClicked() {
		System.out.println("natragButton clicked");

		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../fxml/Godine.fxml"));
			Scene scene = new Scene(root);
			Stage stage = Glavna.window;
			stage.close();
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void obrisiGodinuClicked() {
		System.out.println("obrisiGodinu clicked");
		Integer poljeID;
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Evidencija Polja");
		alert.setHeaderText(null);
		alert.setContentText("Želite li sigurno obrisati godinu: "
				+ GodineController.godinaSelected.getGodina() + "?");
		ButtonType buttonDA = new ButtonType("Da");
		ButtonType buttonNE = new ButtonType("Ne", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonDA, buttonNE);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonDA) {

			Connection veza = null;

			try {
				veza = BazaPodataka.connectToDatabase();
				
				PreparedStatement brisanjeDetaljaPolja = veza.prepareStatement("SELECT POLJEID FROM RAZVOJ.POLJE WHERE GODINAID = ?");
				brisanjeDetaljaPolja.setInt(1, GodineController.godinaSelected.getGodinaID());
				ResultSet rs = brisanjeDetaljaPolja.executeQuery();
				while (rs.next()) {
				poljeID = rs.getInt("POLJEID");
				PreparedStatement obrisiDetaljePolja = veza
						.prepareStatement("DELETE FROM RAZVOJ.PODACIPOLJA WHERE POLJEID = ?");
				obrisiDetaljePolja.setInt(1, poljeID);
				obrisiDetaljePolja.execute();
				}
				
				PreparedStatement obrisiArtikl = veza
						.prepareStatement("DELETE FROM RAZVOJ.CJENIK WHERE GODINAID = ?");
				obrisiArtikl.setInt(1, GodineController.godinaSelected.getGodinaID());
				obrisiArtikl.execute();
				PreparedStatement obrisiPolje = veza
						.prepareStatement("DELETE FROM RAZVOJ.POLJE WHERE GODINAID = ?");
				obrisiPolje.setInt(1, GodineController.godinaSelected.getGodinaID());
				obrisiPolje.execute();
				PreparedStatement obrisiGodinu = veza
						.prepareStatement("DELETE FROM RAZVOJ.GODINA WHERE GODINAID = ? AND KORISNIKID = ? AND GODINA = ?");
				obrisiGodinu.setInt(1,
						GodineController.godinaSelected.getGodinaID());
				obrisiGodinu.setInt(2,
						GodineController.godinaSelected.getKorisnikID());
				obrisiGodinu.setInt(3,
						GodineController.godinaSelected.getGodina());
				obrisiGodinu.execute();
				System.out.println("Godina obrisana: "
						+ GodineController.godinaSelected.getGodina());

				GodineController.godinaSelected.setGodinaID(null);
				GodineController.godinaSelected.setKorisnikID(null);
				GodineController.godinaSelected.setGodina(null);
				GodineController.godinaSelected.setInfoGodina(null);
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

			try {
				Parent root = FXMLLoader.load(getClass().getResource(
						"../fxml/Godine.fxml"));
				Scene scene = new Scene(root);
				Stage stage = Glavna.window;
				stage.close();
				stage.setScene(scene);
				stage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// ne briše se godina
		}

	}

	public void spremiButtonClicked() {
		System.out.println("spremiButton clicked");
		Connection veza = null;
		String godinaInfo = GodinaInfoTextArea.getText();
		try {
			veza = BazaPodataka.connectToDatabase();

			PreparedStatement updateInfoGodina = veza
					.prepareStatement("UPDATE RAZVOJ.GODINA SET INFOGODINA = ? WHERE GODINAID = ? AND KORISNIKID = ? AND GODINA = ?");
			updateInfoGodina.setString(1,
					godinaInfo);
			updateInfoGodina.setInt(2,
					GodineController.godinaSelected.getGodinaID());
			updateInfoGodina.setInt(3,
					GodineController.godinaSelected.getKorisnikID());
			updateInfoGodina.setInt(4,
					GodineController.godinaSelected.getGodina());
			updateInfoGodina.execute();

			GodineController.godinaSelected.setInfoGodina(godinaInfo);
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

	public void izborPoljaClicked() {
		System.out.println("izborPolja clicked");
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

	public void cjenikClicked() {
		System.out.println("cjenik clicked");
		
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
}
