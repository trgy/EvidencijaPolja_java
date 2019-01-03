package diplomski.entiteti;

public class Korisnik {

	private Integer korisnikID;
	private String korisnickoIme;
	private String lozinka;
	private String ime;
	private String prezime;
	private String opg;

	public Korisnik(Integer korisnikID, String korisnickoIme, String lozinka,
			String ime, String prezime, String opg) {
		super();
		this.korisnikID = korisnikID;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.opg = opg;
	}

	public Integer getKorisnikID() {
		return korisnikID;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public String getIme() {
		return ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public String getOpg() {
		return opg;
	}

	public void setKorisnikID(Integer korisnikID) {
		this.korisnikID = korisnikID;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public void setOpg(String opg) {
		this.opg = opg;
	}

}
