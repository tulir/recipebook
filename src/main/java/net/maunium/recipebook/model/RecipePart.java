package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipePart {
	public static Connection db;
	public int order;
	public transient Recipe recipe;
	public int ingredientID;
	public int amount;
	public String unit, instruction;

	public RecipePart(int order, Recipe recipe, int ingredientID, int amount, String unit, String instruction) {
		this.order = order;
		this.recipe = recipe;
		this.ingredientID = ingredientID;
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
			for (int i = 1; i <= parts.size(); i++) {
				RecipePart part = parts.get(i-1);
				part.order = i;
				stmt.setInt(1, part.ingredientID);
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
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM RecipePart WHERE RecipePart.recipe=? ORDER BY RecipePart.order ASC");
			stmt.setInt(1, recipe.id);

			ResultSet results = stmt.executeQuery();
			while(results.next()) {
				int ingredientID = results.getInt("RecipePart.ingredient");
				int order = results.getInt("RecipePart.order");
				int amount = results.getInt("RecipePart.amount");
				String unit = results.getString("RecipePart.unit");
				String instruction = results.getString("RecipePart.instruction");
				parts.add(new RecipePart(order, recipe, ingredientID, amount, unit, instruction));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parts;
	}
}
