package net.maunium.recipebook.api;

import spark.Request;
import spark.Response;

public class Cookbooks {
	public static Object list(Request request, Response response) {
		// TODO list cookbooks.
		// returns List<Recipe>
	}

	public static Object add(Request request, Response response) {
		// TODO add cookbook.
		// returns created Recipe
	}

	public static Object get(Request request, Response response) {
		// TODO get cookbook by id.
		// returns found Recipe
	}

	public static Object edit(Request request, Response response) {
		// TODO edit cookbook.
		// returns edited Recipe
	}

	public static Object delete(Request request, Response response) {
		// TODO delete cookbook.
		return null;
	}
}
