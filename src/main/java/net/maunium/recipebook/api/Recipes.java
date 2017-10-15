// RecipeBook - An Introduction to Databases exercise project with Java Spark and React.
// Copyright (C) 2017  Maunium

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package net.maunium.recipebook.api;

import net.maunium.recipebook.model.Recipe;
import net.maunium.recipebook.util.ErrorMessage;
import net.maunium.recipebook.util.JSON;
import spark.Request;
import spark.Response;

/**
 * Recipes contains handlers for /api/recipe endpoints.
 *
 * @author Tulir Asokan
 */
public class Recipes {
	public static Object list(Request request, Response response) {
		return Recipe.getAll();
	}

	public static Object add(Request request, Response response) {
		Recipe r = JSON.parseJSON(request.body(), Recipe.class);
		r.insert();
		return r;
	}

	private static Recipe getRecipe(Request request) {
		int id;
		try {
			id = Integer.parseInt(request.params("id"));
		} catch (NumberFormatException e) {
			return null;
		}

		return Recipe.get(id);
	}

	public static Object get(Request request, Response response) {
		Recipe r = getRecipe(request);
		if (r == null) {
			response.status(404);
			return new ErrorMessage("Recipe not found");
		}
		return r;
	}

	public static Object edit(Request request, Response response) {
		Recipe oldRecipe = getRecipe(request);
		if (oldRecipe == null) {
			response.status(404);
			return new ErrorMessage("Recipe not found");
		}

		Recipe r = JSON.parseJSON(request.body(), Recipe.class);
		r.id = oldRecipe.id;
		r.update();
		return r;
	}

	public static Object delete(Request request, Response response) {
		Recipe r = getRecipe(request);
		if (r == null) {
			response.status(404);
			return new ErrorMessage("Recipe not found");
		}
		r.delete();
		response.status(204);
		return null;
	}
}
