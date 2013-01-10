package cz.artique.client.listing2;

import cz.artique.shared.utils.HasKey;

public class TestRowData implements HasKey<String> {
	private final String title;
	private final String content;

	public TestRowData(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public String getKey() {
		return getTitle();
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestRowData other = (TestRowData) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
