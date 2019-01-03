package diplomski.entiteti;

public class Polje {
	private Integer poljeID;
	private Integer godinaID;
	private String imePolja;
	private String lokacijaPolja;
	private Double povrsinaPolja;
	private Double prinos;
	private Double cijenaOtkupa;
	private Double potpora;
	private String info;

	public Polje(Integer poljeID, Integer godinaID, String imePolja,
			String lokacijaPolja, Double povrsinaPolja, Double prinos,
			Double cijenaOtkupa, Double potpora, String info) {
		super();
		this.poljeID = poljeID;
		this.godinaID = godinaID;
		this.imePolja = imePolja;
		this.lokacijaPolja = lokacijaPolja;
		this.povrsinaPolja = povrsinaPolja;
		this.prinos = prinos;
		this.cijenaOtkupa = cijenaOtkupa;
		this.potpora = potpora;
		this.info = info;
	}

	public Integer getPoljeID() {
		return poljeID;
	}

	public Integer getGodinaID() {
		return godinaID;
	}

	public String getImePolja() {
		return imePolja;
	}

	public String getLokacijaPolja() {
		return lokacijaPolja;
	}

	public Double getPovrsinaPolja() {
		return povrsinaPolja;
	}

	public Double getPrinos() {
		return prinos;
	}

	public Double getCijenaOtkupa() {
		return cijenaOtkupa;
	}

	public Double getPotpora() {
		return potpora;
	}

	public String getInfo() {
		return info;
	}

	public void setPoljeID(Integer poljeID) {
		this.poljeID = poljeID;
	}

	public void setGodinaID(Integer godinaID) {
		this.godinaID = godinaID;
	}

	public void setImePolja(String imePolja) {
		this.imePolja = imePolja;
	}

	public void setLokacijaPolja(String lokacijaPolja) {
		this.lokacijaPolja = lokacijaPolja;
	}

	public void setPovrsinaPolja(Double povrsinaPolja) {
		this.povrsinaPolja = povrsinaPolja;
	}

	public void setPrinos(Double prinos) {
		this.prinos = prinos;
	}

	public void setCijenaOtkupa(Double cijenaOtkupa) {
		this.cijenaOtkupa = cijenaOtkupa;
	}

	public void setPotpora(Double potpora) {
		this.potpora = potpora;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
