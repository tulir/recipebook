package net.maunium.recipebook.api;

import spark.Request;
import spark.Response;

public class Recipes {
	public static Object list(Request request, Response response) {
		// TODO list recipes.
		// returns List<Recipe>
	}

	public static Object add(Request request, Response response) {
		// TODO add recipe.
		// returns created Recipe
	}

	public static Object get(Request request, Response response) {
		// TODO get recipe by id.
		// returns found Recipe
	}

	public static Object edit(Request request, Response response) {
		// TODO edit recipe.
		// returns edited Recipe
	}

	public static Object delete(Request request, Response response) {
		// TODO delete recipe.
		return null;
	}
}
