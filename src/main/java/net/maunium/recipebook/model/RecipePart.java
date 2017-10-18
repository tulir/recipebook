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
 * RecipePart contains information about a certain part of a recipe.
 *
 * However, it is a linking database table, which means that it does not
 * implement {@link ISQLTableClass} and the methods are intended to be
 * used only by {@link Recipe}.
 *
 * @author Tulir Asokan
 */
public class RecipePart {
	/**
	 * The database connection to use.
	 */
	public static Connection db;
	/**
	 * The recipe this object is a part of.
	 */
	public transient Recipe recipe;
	/**
	 * The ID of the ingredient this part contains.
	 */
	public int ingredientID;
	/**
	 * The amount needed of this ingredient.
	 */
	public double amount;
	/**
	 * The unit of the amount field above.
	 */
	public String unit;
	/**
	 * Short extended instructions about this part of the recipe.
	 */
	public String instructions;

	/**
	 * Initialize a RecipePart with the given data.
	 */
	public RecipePart(Recipe recipe, int ingredientID, double amount, String unit, String instructions) {
		this.recipe = recipe;
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.unit = unit;
		this.instructions = instructions;
	}

	/**
	 * Delete all parts of the given recipe.
	 *
	 * @param recipe The {@link Recipe} whose parts to delete.
	 * @return The amount of changes as returned by {@link PreparedStatement#executeUpdate()}, or {@code -1} if an exception occurred.
	 */
	public static int deleteAll(Recipe recipe) {
		try {
			PreparedStatement stmt = db.prepareStatement(
				"DELETE FROM RecipePart WHERE recipe = ?");
			stmt.setInt(1, recipe.id);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Insert a list of recipe parts for the given recipe.
	 *
	 * @param recipe The {@link Recipe} whose parts to insert.
	 * @param parts The list of parts.
	 * @return The amount of changes as returned by {@link PreparedStatement#executeBatch()}, or an empty array if an exception occurred.
	 */
	public static int[] insertAll(Recipe recipe, List<RecipePart> parts) {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO RecipePart (ingredient, recipe, `order`, amount, unit, instructions) VALUES (?, ?, ?, ?, ?, ?)");
			for (int i = 1; i <= parts.size(); i++) {
				RecipePart part = parts.get(i-1);
				stmt.setInt(1, part.ingredientID);
				stmt.setInt(2, recipe.id);
				stmt.setInt(3, i);
				stmt.setDouble(4, part.amount);
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

	/**
	 * Read the current row of the given {@link ResultSet} into a {@link RecipePart} of the given {@link Recipe}
	 *
	 * @param rs The ResultSet to read.
	 * @param recipe The recipe to give to the RecipePart.
	 * @return A RecipePart with the data from the ResultSet.
	 * @throws SQLException If fetching a column value fails.
	 */
	public static RecipePart read(ResultSet rs, Recipe recipe) throws SQLException {
		int ingredientID = rs.getInt("ingredient");
		double amount = rs.getInt("amount");
		String unit = rs.getString("unit");
		String instructions = rs.getString("instructions");
		return new RecipePart(recipe, ingredientID, amount, unit, instructions);
	}

	/**
	 * Get all parts of the given recipe.
	 *
	 * @param recipe The {@link Recipe} whose parts to get.
	 * @return An {@link ArrayList} containing all the parts of the given recipe.
	 */
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
