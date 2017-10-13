package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public static List<Recipe> getAll(Cookbook cookbook) {
		List<Recipe> recipes = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT Recipe.* FROM CookbookEntry JOIN Recipe ON CookbookEntry.recipe = Recipe.id WHERE CookbookEntry.book = ?");
			stmt.setInt(1, cookbook.id);

			ResultSet results = stmt.executeQuery();
			while(results.next()) {
				int id = results.getInt("Recipe.id");
				String name = results.getString("Recipe.name");
				String description = results.getString("Recipe.description");
				String author = results.getString("Recipe.author");
				Recipe r = new Recipe(id, name, description, author);
				recipes.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}
}
