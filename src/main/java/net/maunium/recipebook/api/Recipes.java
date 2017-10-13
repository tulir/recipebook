package net.maunium.recipebook.api;

import net.maunium.recipebook.model.Recipe;
import net.maunium.recipebook.util.ErrorMessage;
import net.maunium.recipebook.util.JSON;
import spark.Request;
import spark.Response;

public class Recipes {
	public static Object list(Request request, Response response) {
		return Recipe.getAll();
	}

	public static Object add(Request request, Response response) {
		Recipe r = JSON.parseJSON(request.body(), Recipe.class);
		r.insert();
		return r;
	}

	public static Object get(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid recipe ID");
		}

		Recipe r = Recipe.get(id);
		if (r == null) {
			response.status(404);
			return new ErrorMessage("Recipe not found");
		}
		return r;
	}

	public static Object edit(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid recipe ID");
		}

		Recipe r = JSON.parseJSON(request.body(), Recipe.class);
		r.id = id;
		r.update();
		return r;
	}

	public static Object delete(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid recipe ID");
		}

		Recipe r = Recipe.get(id);
		if (r == null) {
			response.status(404);
			return new ErrorMessage("Recipe not found");
		}
		r.delete();
		response.status(204);
		return null;
	}
}
