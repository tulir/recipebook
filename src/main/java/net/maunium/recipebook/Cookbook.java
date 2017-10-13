package net.maunium.recipebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Cookbook implements SQLTableClass {
	protected static Connection db;
	public int id;
	public String name, description, author;
	public List<Recipe> recipes;

	public Cookbook(int id, String name, String description, String author) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
	}

	public void update() {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Cookbook SET name=?, description=?, author=? WHERE id=?");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.setInt(4, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insert() {
		try {
			PreparedStatement stmt = db.prepareStatement("INSERT INTO Cookbook (name, description, author) VALUES (?, ?, ?)");
			stmt.setString(1, name);
			stmt.setString(2, description);
			stmt.setString(3, author);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Cookbook get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Cookbook WHERE id=?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			id = rs.getInt("id");
			String name = rs.getString("name");
			String description = rs.getString("description");
			String author = rs.getString("author");
			return new Cookbook(id, name, description, author);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
