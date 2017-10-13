package net.maunium.recipebook.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cookbook implements ISQLTableClass {
	public static Connection db;
	public int id;
	public String name, description, author;
	public List<Integer> recipes;

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
			int changed = stmt.executeUpdate();

			CookbookEntry.deleteAll(this);
			CookbookEntry.insertAllIDs(this, recipes);
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
			CookbookEntry.insertAllIDs(this, recipes);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try {
			PreparedStatement stmt = db.prepareStatement("DELETE FROM Cookbook WHERE id = ?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			CookbookEntry.deleteAll(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Cookbook read(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		String author = rs.getString("author");
		Cookbook cb = new Cookbook(id, name, description, author);
		cb.recipes = CookbookEntry.getAllIDs(cb);
		return cb;
	}

	public static Cookbook get(int id) {
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Cookbook WHERE id=?");
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

	public static List<Cookbook> getAll() {
		List<Cookbook> cookbooks = new ArrayList<>();
		try {
			PreparedStatement stmt = db.prepareStatement("SELECT * FROM Cookbook");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()) {
				cookbooks.add(read(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cookbooks;
	}
}
