package diplomski.entiteti;

public class Godina {

	private Integer godinaID;
	private Integer korisnikID;
	private Integer godina;
	private String infoGodina;

	public Godina(Integer godinaID, Integer korisnikID, Integer godina,
			String infoGodina) {
		super();
		this.godinaID = godinaID;
		this.korisnikID = korisnikID;
		this.godina = godina;
		this.infoGodina = infoGodina;
	}

	public Integer getGodinaID() {
		return godinaID;
	}

	public Integer getKorisnikID() {
		return korisnikID;
	}

	public Integer getGodina() {
		return godina;
	}

	public String getInfoGodina() {
		return infoGodina;
	}

	public void setGodinaID(Integer godinaID) {
		this.godinaID = godinaID;
	}

	public void setKorisnikID(Integer korisnikID) {
		this.korisnikID = korisnikID;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
	}

	public void setInfoGodina(String infoGodina) {
		this.infoGodina = infoGodina;
	}

}
