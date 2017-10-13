package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Recipe {
	protected static Connection db;
	public int id;
	public int name;

	public static Recipe get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Recipes WHERE id=?");
			stmt.setInt(1, id);
//			ResultSet rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
