package net.maunium.recipebook.api;

import net.maunium.recipebook.model.Ingredient;
import net.maunium.recipebook.util.ErrorMessage;
import net.maunium.recipebook.util.JSON;
import spark.Request;
import spark.Response;

public class IngredientAPI {
	public static Object list(Request request, Response response) {
		return Ingredient.getAll();
	}

	public static Object add(Request request, Response response) {
		Ingredient ingredient = JSON.parseJSON(request.body(), Ingredient.class);
		ingredient.insert();
		return ingredient;
	}

	public static Object rename(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid ingredient ID");
		}

		Ingredient ingredient = JSON.parseJSON(request.body(), Ingredient.class);
		ingredient.id = id;
		ingredient.update();
		return ingredient;
	}

	public static Object delete(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid ingredient ID");
		}

		Ingredient ingredient = Ingredient.get(id);
		ingredient.delete();
		response.status(204);
		return null;
	}
}
