package diplomski.entiteti;

public class Cjenik {
	private Integer artiklID;
	private Integer godinaID;
	private Integer tipartiklaID;
	private String imeArtikla;
	private Double cijenaArtikla;
	private String datumCijene;

	public Cjenik(Integer artiklID, Integer godinaID, Integer tipartiklaID,
			String imeArtikla, Double cijenaArtikla, String datumCijene) {
		super();
		this.artiklID = artiklID;
		this.godinaID = godinaID;
		this.tipartiklaID = tipartiklaID;
		this.imeArtikla = imeArtikla;
		this.cijenaArtikla = cijenaArtikla;
		this.datumCijene = datumCijene;
	}

	public Integer getArtiklID() {
		return artiklID;
	}

	public Integer getGodinaID() {
		return godinaID;
	}

	public Integer getTipartiklaID() {
		return tipartiklaID;
	}

	public String getImeArtikla() {
		return imeArtikla;
	}

	public Double getCijenaArtikla() {
		return cijenaArtikla;
	}

	public String getDatumCijene() {
		return datumCijene;
	}

	public void setArtiklID(Integer artiklID) {
		this.artiklID = artiklID;
	}

	public void setGodinaID(Integer godinaID) {
		this.godinaID = godinaID;
	}

	public void setTipartiklaID(Integer tipartiklaID) {
		this.tipartiklaID = tipartiklaID;
	}

	public void setImeArtikla(String imeArtikla) {
		this.imeArtikla = imeArtikla;
	}

	public void setCijenaArtikla(Double cijenaArtikla) {
		this.cijenaArtikla = cijenaArtikla;
	}

	public void setDatumCijene(String datumCijene) {
		this.datumCijene = datumCijene;
	}

}
