package hr.fer.zemris.java.hw_13.voting;

/**
 * Class containing informations about the band.
 * 
 * @author Marin Maršić
 *
 */
public class Band {

	/**
	 * ID of the band.
	 */
	private String iD;
	/**
	 * Name of the band.
	 */
	private String name;
	/**
	 * Short url of their representing song.
	 */
	private String url;

	/**
	 * Constructor for the {@link Band}.
	 * 
	 * @param iD
	 *            ID of the band.
	 * @param name
	 *            Name of the band.
	 * @param url
	 *            Url of their representing song.
	 */
	public Band(String iD, String name, String url) {
		this.iD = iD;
		this.name = name;
		this.url = url;
	}

	/**
	 * Constructor for the {@link Band} which reads arguments from given line of
	 * text. ID name and url separated by TAB.
	 * 
	 * @param line Line containing informations about the text.
	 */
	public Band(String line) {
		String[] parameters = line.split("\t");
		this.iD = parameters[0];
		this.name = parameters[1];
		this.url = parameters[2];
	}

	/**
	 * @return Returns ID.
	 */
	public String getiD() {
		return iD;
	}

	/**
	 * @return Returns name of the band.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns url of their representing song.
	 */
	public String getUrl() {
		return url;
	}
}
