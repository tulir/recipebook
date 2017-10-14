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

package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CookbookEntry is an utility class to access recipes in a cookbook.
 *
 * @author Tulir Asokan
 */
public class CookbookEntry {
	public static Connection db;

	public static int deleteAll(Cookbook cookbook) {
		try {
			PreparedStatement stmt = db.prepareStatement(
				"DELETE FROM CookbookEntry WHERE cookbook = ?");
			stmt.setInt(1, cookbook.id);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int[] insertAll(Cookbook cookbook, List<Recipe> recipes) {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO CookbookEntry VALUES (?, ?)");
			for (Recipe recipe : recipes) {
				stmt.setInt(1, cookbook.id);
				stmt.setInt(2, recipe.id);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return new int[]{};
		}
	}

	public static int[] insertAllIDs(Cookbook cookbook, List<Integer> recipeIDs) {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO CookbookEntry VALUES (?, ?)");
			for (int recipeID : recipeIDs) {
				stmt.setInt(1, cookbook.id);
				stmt.setInt(2, recipeID);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return new int[]{};
		}
	}

	public static List<Recipe> getAll(Cookbook cookbook) {
		List<Recipe> recipes = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT Recipe.* FROM CookbookEntry JOIN Recipe ON CookbookEntry.recipe = Recipe.id WHERE CookbookEntry.book = ?");
			stmt.setInt(1, cookbook.id);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				recipes.add(Recipe.read(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

	public static List<Integer> getAllIDs(Cookbook cookbook) {
		List<Integer> recipeIDs = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT Recipe.id FROM CookbookEntry JOIN Recipe ON CookbookEntry.recipe = Recipe.id WHERE CookbookEntry.book = ?");
			stmt.setInt(1, cookbook.id);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				recipeIDs.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipeIDs;
	}
}
