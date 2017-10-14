package net.maunium.recipebook.util;

/**
 * ErrorMessage is a simple error message intended for JSON serialization.
 *
 * This is intended to be returned from API endpoint handlers that use the
 * {@link JSON#transformer()} response transformer.
 *
 * @author Tulir Asokan
 * @project RecipeBook
 */
public class ErrorMessage {
	/**
	 * The state of the app when this message is returned.
	 */
	public final String state = "error";
	/**
	 * A description of the error that occurred.
	 */
	public String message;

	public ErrorMessage(String message) {
		this.message = message;
	}
}
