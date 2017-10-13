package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient {
	protected static Connection db;
	public int id;
	public int name;

	public static Ingredient get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Ingredients WHERE id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}
}
