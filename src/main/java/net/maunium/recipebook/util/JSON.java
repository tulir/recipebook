package net.maunium.recipebook.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * JSON offers simple static functions to access Gson.
 *
 * @author Tulir Asokan
 * @project RecipeBook
 */
public class JSON {
	/**
	 * The GSON instance that is used by the static methods in this class.
	 */
	public static final Gson gson = new Gson();

	/**
	 * Convert an object into a JSON string.
	 *
	 * @param object The object to convert.
	 * @return A JSON string.
	 */
	public static String toJSON(Object object) {
		return gson.toJson(object);
	}

	/**
	 * Parse a JSON string into an object of the given type.
	 * @param json The JSON string.
	 * @param type The class which to parse the JSON into.
	 * @return The parsed object.
	 */
	public static <T> T parseJSON(String json, Class<T> type) {
		return gson.fromJson(json, type);
	}

	/**
	 * @return A Spark {@link ResponseTransformer} that converts responses to JSON.
	 */
	public static ResponseTransformer transformer() {
		return JSON::toJSON;
	}
}
