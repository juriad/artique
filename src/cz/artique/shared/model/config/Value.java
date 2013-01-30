package cz.artique.shared.model.config;

import java.io.Serializable;

public class Value implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer i;
	private Double d;
	private String s;
	private ConfigType type;

	public Value() {}

	public Value(Integer i) {
		this.setI(i);
		setType(ConfigType.INT);
	}

	public Value(Double d) {
		this.setD(d);
		setType(ConfigType.DOUBLE);
	}

	public Value(String s) {
		this.setS(s);
		setType(ConfigType.STRING);
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}
}
