package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements ISQLTableClass {
	public static Connection db;
	public int id;
	public String name, description, author, instructions;
	public List<RecipePart> parts;

	public Recipe(int id, String name, String description, String author, String instructions) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.instructions = instructions;
	}

	public void update() {
		update(true);
	}

	public void update(boolean partsChanged) {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Recipe SET name=?, description=?, author=?, instructions=? WHERE id = ?");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.setString(4, instructions);
			stmt.setInt(4, id);
			stmt.executeUpdate();
			if (partsChanged) {
				RecipePart.deleteAll(this);
				RecipePart.insertAll(this, parts);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert() {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO Recipe (name, description, author, instructions) VALUES (?, ?, ?, ?)");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.setString(4, instructions);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			RecipePart.insertAll(this, parts);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			PreparedStatement stmt = db.prepareStatement("DELETE FROM Recipe WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Recipe read(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		String author = rs.getString("author");
		String instructions = rs.getString("instructions");
		Recipe r = new Recipe(id, name, description, author, instructions);
		r.parts = RecipePart.getAll(r);
		return r;
	}

	public static Recipe get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Recipe WHERE id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return read(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Recipe> getAll() {
		List<Recipe> recipes = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Recipe");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				recipes.add(read(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}

	public static List<Recipe> getAll() {
		List<Recipe> recipes = new ArrayList<Recipe>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Recipe");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String description = rs.getString("description");
				String author = rs.getString("author");
				recipes.add(new Recipe(id, name, description, author;));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recipes;
	}
}
