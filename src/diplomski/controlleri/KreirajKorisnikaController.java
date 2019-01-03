package diplomski.controlleri;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import diplomski.database.BazaPodataka;
import diplomski.database.Enkripcija;
import diplomski.glavna.Glavna;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class KreirajKorisnikaController {
	
	@FXML
	public TextField korisnikTextField, lozinkaTextField, imeTextField, prezimeTextField, opgTextField;
	
	public void spremiButtonClicked() {
		
		
		System.out.println("spremiButton clicked");
		
		Integer korisnikID = null;
		String korisnickoIme = korisnikTextField.getText().trim();
		String lozinka = Enkripcija.MD5(lozinkaTextField.getText().trim());
		String ime = imeTextField.getText().trim();
		String prezime = prezimeTextField.getText().trim();
		String opg = opgTextField.getText().trim();
		
		Connection veza = null;
		
		try{
			veza = BazaPodataka.connectToDatabase();
			//traži da li je korisnièko ime zauzeto
			PreparedStatement traziSlobodniUser = veza.prepareStatement("SELECT KORISNIKID FROM RAZVOJ.KORISNIK WHERE KORISNICKOIME = ?");
			traziSlobodniUser.setString(1, korisnikTextField.getText());
			ResultSet rs = traziSlobodniUser.executeQuery();
			while (rs.next()) {
			korisnikID = rs.getInt("KORISNIKID");
			}
			
			//Unosi podatke ukoliko je korisnièko ime slobodno i ukoliko je korisnik unio obavezne podatke
			if(korisnikID == null){
				if (korisnickoIme.equals("") || lozinka.equals("")) {
					System.out.println("Korisnik treba obavezno unijeti korisnièko ime i lozinku!! ");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Evidencija Polja");
					alert.setHeaderText("Obavezno unesite korisnièko ime i lozinku!");
					alert.setContentText("Polja Korisnièko ime i lozinka su Vam prazni.");

					alert.showAndWait();
				}
				else{
				System.out.println("Unos korisnika... ");
			PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO RAZVOJ.KORISNIK "
					+ "(KORISNICKOIME, LOZINKA, IME, PREZIME,OPG) VALUES (?, ?, ?, ?, ?)");
			preparedStatement.setString(1, korisnickoIme);
			preparedStatement.setString(2, lozinka);
			preparedStatement.setString(3, ime);
			preparedStatement.setString(4, prezime);
			preparedStatement.setString(5, opg);
			preparedStatement.execute();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Evidencija Polja");
			alert.setHeaderText("Kreirali ste profil " + korisnickoIme);
			alert.setContentText("Ime: " + ime + "\nPrezime: " + prezime + "\nOPG ili poduzeæe: " + opg);

			alert.showAndWait();
			
			try{
				Parent root = FXMLLoader.load(getClass().getResource("../fxml/Ulaz.fxml"));
				Scene scene = new Scene (root);
				Stage stage = Glavna.window;
				stage.close();
				stage.setScene(scene);
				stage.show();
				
				}
				catch (Exception e){
					e.printStackTrace();
				}
				}
			}
			//Javlja korisniku da je korisnièko ime zauzeto
			
			else {
				System.out.println("Korisnièko ime je zauzeto!! ");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Evidencija Polja");
				alert.setHeaderText("Korisnièko ime zauzeto!");
				alert.setContentText("Pokušajte novo korisnièko ime...");

				alert.showAndWait();
			}
			
			} catch(IOException e) {
			e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
			try{
				BazaPodataka.closeConnectionToDatabase(veza);
			} catch(SQLException ex) {
			ex.printStackTrace();
			 }
			}
	}

	public void natragButtonClicked() {
		
		System.out.println("natragButton clicked");
		
		try{
			Parent root = FXMLLoader.load(getClass().getResource("../fxml/Ulaz.fxml"));
			Scene scene = new Scene (root);
			Stage stage = Glavna.window;
			stage.close();
			stage.setScene(scene);
			stage.show();
			
			}
			catch (Exception e){
				e.printStackTrace();
			}
	}
}
