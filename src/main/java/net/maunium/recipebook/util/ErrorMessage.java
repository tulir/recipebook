package net.maunium.recipebook.util;

/**
 * ErrorMessage is a simple error message intended for JSON serialization.
 *
 * @author Tulir Asokan
 * @project RecipeBook
 */
public class ErrorMessage {
	public final String state = "error";
	public String message;

	public ErrorMessage(String message) {
		this.message = message;
	}
}
