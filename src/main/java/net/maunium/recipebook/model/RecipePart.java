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
	public String unit, instructions;

	public RecipePart(int order, Recipe recipe, int ingredientID, int amount, String unit, String instructions) {
		this.order = order;
		this.recipe = recipe;
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.unit = unit;
		this.instructions = instructions;
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
			PreparedStatement stmt = db.prepareStatement("INSERT INTO RecipePart (ingredient, recipe, `order`, amount, unit, instructions) VALUES (?, ?, ?, ?, ?, ?)");
			for (int i = 1; i <= parts.size(); i++) {
				RecipePart part = parts.get(i-1);
				part.order = i;
				stmt.setInt(1, part.ingredientID);
				stmt.setInt(2, recipe.id);
				stmt.setInt(3, part.order);
				stmt.setInt(4, part.amount);
				stmt.setString(5, part.unit);
				stmt.setString(6, part.instructions);
				stmt.addBatch();
			}
			return stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			return new int[]{};
		}
	}

	public static RecipePart read(ResultSet rs, Recipe recipe) throws SQLException {
		int ingredientID = rs.getInt("ingredient");
		int order = rs.getInt("order");
		int amount = rs.getInt("amount");
		String unit = rs.getString("unit");
		String instructions = rs.getString("instructions");
		return new RecipePart(order, recipe, ingredientID, amount, unit, instructions);
	}

	public static List<RecipePart> getAll(Recipe recipe) {
		List<RecipePart> parts = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM RecipePart WHERE RecipePart.recipe=? ORDER BY RecipePart.`order` ASC");
			stmt.setInt(1, recipe.id);

			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				parts.add(read(rs, recipe));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parts;
	}
}
