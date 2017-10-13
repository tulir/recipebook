package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient {
	protected static Connection db;
	public int id;
	public String name;

	public Ingredient(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static Ingredient get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Ingredients WHERE id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			id = rs.getInt("id");
			String name = rs.getString("name");
			return new Ingredient(id, name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
