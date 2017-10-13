package net.maunium.recipebook.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JSON {
	private static final Gson gson = new Gson();

	public static String toJSON(Object object) {
		return gson.toJson(object);
	}

	public static <T> T parseJSON(String json, Class<T> type) {
		return gson.fromJson(json, type);
	}

	public static ResponseTransformer transformer() {
		return JSON::toJSON;
	}
}