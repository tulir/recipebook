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
 * Cookbook contains information about a single cookbook.
 *
 * @author Tulir Asokan
 */
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

	public void write(PreparedStatement stmt, boolean writeID) throws SQLException {
		stmt.setString(1, name);
		stmt.setString(2, description);
		stmt.setString(3, author);
		if (writeID) {
			stmt.setInt(4, id);
		}
	}

	public void update() {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Cookbook SET name=?, description=?, author=? WHERE id=?");
			write(stmt, true);
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
			write(stmt, false);
			id = ISQLTableClass.insertAndGetID(stmt);
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
