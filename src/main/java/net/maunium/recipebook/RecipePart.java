package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipePart {
	protected static Connection db;
	public int order;
	public Recipe recipe;
	public Ingredient ingredient;
	public int amount;
	public String unit, instruction;

	public RecipePart(int order, Recipe recipe, Ingredient ingredient, int amount, String unit, String instruction) {
		this.order = order;
		this.recipe = recipe;
		this.ingredient = ingredient;
		this.amount = amount;
		this.unit = unit;
		this.instruction = instruction;
	}

	public static int deleteAll(Recipe recipe) {
		try {
			PreparedStatement stmt = db.prepareStatement(
				"DELETE FROM RecipePart WHERE recipe = ?");
			stmt.setInt(1, recipe.id);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static int[] insertAll(Recipe recipe, List<RecipePart> parts) {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO RecipePart (ingredient, recipe, order, amount, unit, instruction) VALUES (?, ?, ?, ?, ?, ?)");
			for (int i = 0; i < parts.size(); i++) {
				RecipePart part = parts.get(i);
				part.order = i;
				stmt.setInt(1, part.ingredient.id);
				stmt.setInt(2, recipe.id);
				stmt.setInt(3, part.order);
				stmt.setInt(4, part.amount);
				stmt.setString(5, part.unit);
				stmt.setString(6, part.instruction);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return new int[]{};
		}
	}

	public static List<RecipePart> getAll(Recipe recipe) {
		List<RecipePart> parts = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM RecipePart, Ingredient WHERE RecipePart.recipe=? AND RecipePart.ingredient = Ingredient.id");
			stmt.setInt(1, recipe.id);

			ResultSet results = stmt.executeQuery();
			while(results.next()) {
				int order = results.getInt("RecipePart.order");
				int amount = results.getInt("RecipePart.amount");
				String unit = results.getString("RecipePart.unit");
				String instruction = results.getString("RecipePart.instruction");

				int ingredientID = results.getInt("Ingredient.id");
				String ingredientName = results.getString("Ingredient.name");
				Ingredient ingredient = new Ingredient(ingredientID, ingredientName);

				parts.add(new RecipePart(order, recipe, ingredient, amount, unit, instruction));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parts;
	}
}
