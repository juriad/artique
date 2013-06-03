package cz.artique.client.shortcuts;

import java.util.HashMap;
import java.util.Map;

public enum KeyCodesEnum {

	KEY_ALT(18),

	KEY_BACKSPACE(8),

	KEY_CTRL(17),

	KEY_DELETE(46),

	KEY_DOWN(40),

	KEY_END(35),

	KEY_ENTER(13),

	KEY_ESCAPE(27),

	KEY_HOME(36),

	KEY_LEFT(37),

	KEY_PAGEDOWN(34),

	KEY_PAGEUP(33),

	KEY_RIGHT(39),

	KEY_SHIFT(16),

	KEY_TAB(9),

	KEY_UP(38);

	private static final Map<String, KeyCodesEnum> lookup =
		new HashMap<String, KeyCodesEnum>();
	static {
		for (KeyCodesEnum e : KeyCodesEnum.values()) {
			lookup.put(e.name(), e);
			lookup.put(e.name().substring(4), e);
		}
	}

	private final int code;

	private KeyCodesEnum(int code) {
		this.code = code;
	}

	public static KeyCodesEnum getByName(String name) {
		return lookup.get(name);
	}

	public int getCode() {
		return code;
	}
}
