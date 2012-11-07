package cz.artique.shared.model.label;

public class LabelApperianceImpl implements LabelApperiance {

	VisibilityLevel visibilityLevel;

	ApperianceType apperianceType;

	String assignedIcon = null;

	String unAssignedIcon = null;

	String assignedTextColor = null;

	String unAssignedTextColor = null;

	String assignedTextBackground = null;

	String unAssignedTextBackground = null;

	public LabelApperianceImpl() {}
	public LabelApperianceImpl(VisibilityLevel visibilityLevel,
			ApperianceType apperianceType) {
		setVisibilityLevel(visibilityLevel);
		setApperianceType(apperianceType);
	}
	public ApperianceType getApperianceType() {
		return apperianceType;
	}
	public String getAssignedIcon() {
		return assignedIcon;
	}
	public String getAssignedTextBackground() {
		return assignedTextBackground;
	}
	public String getAssignedTextColor() {
		return assignedTextColor;
	}
	public String getUnAssignedIcon() {
		return unAssignedIcon;
	}
	public String getUnAssignedTextBackground() {
		return unAssignedTextBackground;
	}

	public String getUnAssignedTextColor() {
		return unAssignedTextColor;
	}

	public VisibilityLevel getVisibilityLevel() {
		return visibilityLevel;
	}

	public void setApperianceType(ApperianceType apperianceType) {
		this.apperianceType = apperianceType;
	}

	public void setAssignedIcon(String assignedIcon) {
		this.assignedIcon = assignedIcon;
	}

	public void setAssignedTextBackground(String assignedTextBackground) {
		this.assignedTextBackground = assignedTextBackground;
	}

	public void setAssignedTextColor(String assignedTextColor) {
		this.assignedTextColor = assignedTextColor;
	}

	public void setUnAssignedIcon(String unAssignedIcon) {
		this.unAssignedIcon = unAssignedIcon;
	}

	public void setUnAssignedTextBackground(String unAssignedTextBackground) {
		this.unAssignedTextBackground = unAssignedTextBackground;
	}

	public void setUnAssignedTextColor(String unAssignedTextColor) {
		this.unAssignedTextColor = unAssignedTextColor;
	}

	public void setVisibilityLevel(VisibilityLevel visibilityLevel) {
		this.visibilityLevel = visibilityLevel;
	}

}
