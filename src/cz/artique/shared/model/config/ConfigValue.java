package cz.artique.shared.model.config;

import java.io.Serializable;

/**
 * Stores config value of all types together with its type.
 * 
 * @author Adam Juraszek
 * 
 */
public class ConfigValue implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer i;
	private Double d;
	private String s;
	private ConfigType type;

	/**
	 * Constructor for deserialization.
	 */
	public ConfigValue() {}

	/**
	 * @param i
	 *            value of type INT
	 */
	public ConfigValue(Integer i) {
		this.setI(i);
		setType(ConfigType.INT);
	}

	/**
	 * @param d
	 *            value fo type DOUBLE
	 */
	public ConfigValue(Double d) {
		this.setD(d);
		setType(ConfigType.DOUBLE);
	}

	/**
	 * @param s
	 *            value of type STRING
	 */
	public ConfigValue(String s) {
		this.setS(s);
		setType(ConfigType.STRING);
	}

	/**
	 * @return integer value
	 */
	public Integer getI() {
		return i;
	}

	/**
	 * @param i
	 *            integer value
	 */
	public void setI(Integer i) {
		this.i = i;
	}

	/**
	 * @return double value
	 */
	public Double getD() {
		return d;
	}

	/**
	 * @param d
	 *            double value
	 */
	public void setD(Double d) {
		this.d = d;
	}

	/**
	 * @return string value
	 */
	public String getS() {
		return s;
	}

	/**
	 * @param s
	 *            string value
	 */
	public void setS(String s) {
		this.s = s;
	}

	/**
	 * @return type of value
	 */
	public ConfigType getType() {
		return type;
	}

	/**
	 * @param type
	 *            type of value
	 */
	public void setType(ConfigType type) {
		this.type = type;
	}
}
