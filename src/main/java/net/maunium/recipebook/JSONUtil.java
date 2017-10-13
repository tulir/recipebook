package net.maunium.recipebook;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JSONUtil {
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}

	public static ResponseTransformer transformer() {
		return JSONUtil::toJson;
	}
}
