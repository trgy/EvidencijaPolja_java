package diplomski.entiteti;

public class PodaciPolja {
	private Integer podaciPoljaID;
	private Integer poljeID;
	private String tipArtikla;
	private Double cijenaArtikla;
	private Double kolicina;
	private Double trosak;

	public PodaciPolja(Integer podaciPoljaID, Integer poljeID,
			String tipArtikla, Double cijenaArtikla, Double kolicina,
			Double trosak) {
		super();
		this.podaciPoljaID = podaciPoljaID;
		this.poljeID = poljeID;
		this.tipArtikla = tipArtikla;
		this.cijenaArtikla = cijenaArtikla;
		this.kolicina = kolicina;
		this.trosak = trosak;
	}

	public Integer getPodaciPoljaID() {
		return podaciPoljaID;
	}

	public Integer getPoljeID() {
		return poljeID;
	}

	public String getTipArtikla() {
		return tipArtikla;
	}

	public Double getCijenaArtikla() {
		return cijenaArtikla;
	}

	public Double getKolicina() {
		return kolicina;
	}

	public Double getTrosak() {
		return trosak;
	}

	public void setPodaciPoljaID(Integer podaciPoljaID) {
		this.podaciPoljaID = podaciPoljaID;
	}

	public void setPoljeID(Integer poljeID) {
		this.poljeID = poljeID;
	}

	public void setTipArtikla(String tipArtikla) {
		this.tipArtikla = tipArtikla;
	}

	public void setCijenaArtikla(Double cijenaArtikla) {
		this.cijenaArtikla = cijenaArtikla;
	}

	public void setKolicina(Double kolicina) {
		this.kolicina = kolicina;
	}

	public void setTrosak(Double trosak) {
		this.trosak = trosak;
	}

}
