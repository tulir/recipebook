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

import net.maunium.recipebook.model.Ingredient;
import net.maunium.recipebook.util.ErrorMessage;
import net.maunium.recipebook.util.JSON;
import spark.Request;
import spark.Response;

/**
 * Ingredients contains handlers for /api/ingredient/ endpoints.
 *
 * @author Tulir Asokan
 */
public class Ingredients {
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
		if (ingredient == null) {
			response.status(404);
			return new ErrorMessage("Ingredient not found");
		}
		ingredient.delete();
		response.status(204);
		return null;
	}
}
