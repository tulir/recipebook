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

package net.maunium.recipebook.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * JSON offers simple static functions to access Gson.
 *
 * @author Tulir Asokan
 */
public class JSON {
	/**
	 * The GSON instance that is used by the static methods in this class.
	 */
	public static final Gson gson = new Gson();

	/**
	 * Convert an object into a JSON string.
	 *
	 * @param object The object to convert.
	 * @return A JSON string.
	 */
	public static String toJSON(Object object) {
		return gson.toJson(object);
	}

	/**
	 * Parse a JSON string into an object of the given type.
	 * @param json The JSON string.
	 * @param type The class which to parse the JSON into.
	 * @return The parsed object.
	 */
	public static <T> T parseJSON(String json, Class<T> type) {
		return gson.fromJson(json, type);
	}

	/**
	 * @return A Spark {@link ResponseTransformer} that converts responses to JSON.
	 */
	public static ResponseTransformer transformer() {
		return JSON::toJSON;
	}
}
