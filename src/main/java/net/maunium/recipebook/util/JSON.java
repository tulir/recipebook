package net.maunium.recipebook.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JSON {
	public static String toJSON(Object object) {
		return new Gson().toJson(object);
	}

	public static ResponseTransformer transformer() {
		return JSON::toJSON;
	}
}
