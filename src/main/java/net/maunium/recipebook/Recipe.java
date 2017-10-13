package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements SQLTableClass {
	protected static Connection db;
	public int id;
	public String name, description, author;
	public List<RecipePart> parts;

	public Recipe(int id, String name, String description, String author) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
	}

	public void update() {
		update(true);
	}

	public void update(boolean partsChanged) {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Recipe SET name=?, description=?, author=? WHERE id = ?");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.setInt(4, id);
			stmt.executeUpdate();
			if (partsChanged) {
				RecipePart.deleteAll(id);
				RecipePart.insertAll(id, parts);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert() {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO Recipe (name, description, author) VALUES (?, ?, ?)");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			RecipePart.insertAll(id, parts);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Recipe get(int id) {
		return get(id, true);
	}

	public static Recipe get(int id, boolean getParts) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Recipe WHERE id=?");
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
			id = rs.getInt("id");
			String name = rs.getString("name");
			String description = rs.getString("description");
			String author = rs.getString("author");

			Recipe r = new Recipe(id, name, description, author);
			if (getParts) {
				r.parts = RecipePart.getAll(r.id);
			}
			return r;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
