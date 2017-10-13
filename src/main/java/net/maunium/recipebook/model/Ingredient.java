package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ingredient implements ISQLTableClass {
	public static Connection db;
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

	public void delete() {
		try {
			PreparedStatement stmt = db.prepareStatement("DELETE FROM Ingredient WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
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

	public static List<Ingredient> getAll() {
		List<Ingredient> ingredients = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Ingredient");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				ingredients.add(new Ingredient(id, name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ingredients;
	}
}
