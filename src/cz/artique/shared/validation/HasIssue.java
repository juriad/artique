package cz.artique.shared.validation;

/**
 * Validation messages are backed by enums which define all possible causes of
 * issues. Unfortunately, the method {@link Class#getSimpleName()} is not
 * available in GWT code. Therefore, it is emulated by hard-coded string
 * constant returned by {@link #enumName()} method. This constant is used by
 * message system on client to identify appropriate message to show.
 * 
 * <p>
 * All validation enums has the same name as the service method they belong to,
 * except the first capital letter. There always exists exactly one enum for
 * each service method.
 * 
 * @author Adam Juraszek
 * 
 */
public interface HasIssue {
	/**
	 * Emulates unavailable {@link Class#getSimpleName()} method in GWT code.
	 * 
	 * @return hard-coded string name; the same the method
	 *         {@link Class#getSimpleName()} would return
	 */
	String enumName();
}
