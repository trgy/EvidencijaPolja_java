package diplomski.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class BazaPodataka {

	private static final String DATABASE_FILE = "database.properties";

	public static Connection connectToDatabase() throws SQLException, IOException {
		Properties svojstva = new Properties();
		svojstva.load(new FileReader(DATABASE_FILE));
		String urlBazePodataka = svojstva.getProperty("bazaPodatakaUrl");
		String korisnickoIme = svojstva.getProperty("korisnickoIme");
		String lozinka = svojstva.getProperty("lozinka");
		Connection veza = DriverManager.getConnection(urlBazePodataka,
				korisnickoIme, lozinka);
		return veza;
	}

	public static void closeConnectionToDatabase(final Connection p_connection) throws SQLException {
		p_connection.close();
	}
	
	
}
