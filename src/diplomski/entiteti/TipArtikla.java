package diplomski.entiteti;

public class TipArtikla {
	private Integer tipArtiklaID;
	private String tipArtikla;
	private String proizvodac;
	private Integer godinaNabave;
	private String dodatniInfo;

	public TipArtikla(Integer tipArtiklaID, String tipArtikla,
			String proizvodac, Integer godinaNabave, String dodatniInfo) {
		super();
		this.tipArtiklaID = tipArtiklaID;
		this.tipArtikla = tipArtikla;
		this.proizvodac = proizvodac;
		this.godinaNabave = godinaNabave;
		this.dodatniInfo = dodatniInfo;
	}

	public Integer getTipArtiklaID() {
		return tipArtiklaID;
	}

	public String getTipArtikla() {
		return tipArtikla;
	}

	public String getProizvodac() {
		return proizvodac;
	}

	public Integer getGodinaNabave() {
		return godinaNabave;
	}

	public String getDodatniInfo() {
		return dodatniInfo;
	}

	public void setTipArtiklaID(Integer tipArtiklaID) {
		this.tipArtiklaID = tipArtiklaID;
	}

	public void setTipArtikla(String tipArtikla) {
		this.tipArtikla = tipArtikla;
	}

	public void setProizvodac(String proizvodac) {
		this.proizvodac = proizvodac;
	}

	public void setGodinaNabave(Integer godinaNabave) {
		this.godinaNabave = godinaNabave;
	}

	public void setDodatniInfo(String dodatniInfo) {
		this.dodatniInfo = dodatniInfo;
	}

}
