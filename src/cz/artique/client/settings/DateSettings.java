package cz.artique.client.settings;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

public class DateSettings {
	public enum DateShowOption {
		SHOW_ADDED,
		SHOW_PUBLISHED
	}

	private TimeZone timeZone;
	private DateShowOption dateShowOption;
	private DateTimeFormat abbrFormat;
	private DateTimeFormat fullFormat;

	public DateSettings(TimeZone timeZone, DateShowOption dateShowOption) {
		this.timeZone = timeZone;
		this.dateShowOption = dateShowOption;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public DateShowOption getDateShowOption() {
		return dateShowOption;
	}

	public void setDateShowOption(DateShowOption dateShowOption) {
		this.dateShowOption = dateShowOption;
	}

	public DateTimeFormat getAbbrFormat() {
		return abbrFormat;
	}

	public void setAbbrFormat(DateTimeFormat abbrFormat) {
		this.abbrFormat = abbrFormat;
	}

	public DateTimeFormat getFullFormat() {
		return fullFormat;
	}

	public void setFullFormat(DateTimeFormat fullFormat) {
		this.fullFormat = fullFormat;
	}
}
