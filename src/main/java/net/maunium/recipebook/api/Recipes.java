package net.maunium.recipebook.api;

import net.maunium.recipebook.model.Recipe;
import spark.Request;
import spark.Response;

public class Recipes {
	public static Object list(Request request, Response response) {
		return Recipe.getAll();
	}

	public static Object add(Request request, Response response) {
    	Recipe recipe = JSON.parseJSON(request.body(), Recipe.class);
    	recipe.insert();
    	return recipe;
	}

	public static Object get(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid recipe ID");
		}

		Recipe recipe = JSON.parseJSON(request.body(), Recipe.class);
		recipe.id = id;
		recipe.update();
		return recipe;
	}

	public static Object edit(Request request, Response response) {
		// TODO edit recipe.
		// returns edited Recipe
	}

	public static Object delete(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid recipe ID");
		}

		Recipe recipe = Recipe.get(id);
		recipe.delete();
		response.status(204);
		return null;
	}
}
