package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ingredient implements SQLTableClass {
	protected static Connection db;
	public int id;
	public String name;

	public Ingredient(String name) {
		this(-1, name);
	}

	public Ingredient(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void update() {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Ingredient SET name=? WHERE id=?");
			stmt.setString(1, name);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert() {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO Ingredient (name) VALUES (?)");
			stmt.setString(1, name);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Ingredient get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Ingredient WHERE id=?");
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
