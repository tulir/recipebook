package net.maunium.recipebook.api;

import net.maunium.recipebook.model.Cookbook;
import net.maunium.recipebook.util.ErrorMessage;
import net.maunium.recipebook.util.JSON;
import spark.Request;
import spark.Response;

public class Cookbooks {
	public static Object list(Request request, Response response) {
		return Cookbook.getAll();
	}

	public static Object add(Request request, Response response) {
		Cookbook cb = JSON.parseJSON(request.body(), Cookbook.class);
		cb.insert();
		return cb;
	}

	public static Object get(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid cookbook ID");
		}

		Cookbook cb = Cookbook.get(id);
		if (cb == null) {
			response.status(404);
			return new ErrorMessage("Cookbook not found");
		}
		return cb;
	}

	public static Object edit(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid cookbook ID");
		}

		Cookbook cb = JSON.parseJSON(request.body(), Cookbook.class);
		cb.id = id;
		cb.update();
		return cb;
	}

	public static Object delete(Request request, Response response) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			response.status(400);
			return new ErrorMessage("Invalid cookbook ID");
		}

		Cookbook cb = Cookbook.get(id);
		if (cb == null) {
			response.status(404);
			return new ErrorMessage("Cookbook not found");
		}
		cb.delete();
		response.status(204);
		return null;
	}
}
