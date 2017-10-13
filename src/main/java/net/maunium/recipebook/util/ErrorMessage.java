package net.maunium.recipebook.util;

public class ErrorMessage {
	public final String state = "error";
	public String message;

	public ErrorMessage(String message) {
		this.message = message;
	}
}
