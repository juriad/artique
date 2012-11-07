package cz.artique.shared.model.label;

public interface LabelApperiance {
	ApperianceType getApperianceType();

	String getAssignedIcon();

	String getAssignedTextBackground();

	String getAssignedTextColor();

	String getUnAssignedIcon();

	String getUnAssignedTextBackground();

	String getUnAssignedTextColor();

	VisibilityLevel getVisibilityLevel();

}
