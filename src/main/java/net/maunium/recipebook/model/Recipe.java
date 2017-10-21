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
 * Recipe contains information about a single recipe.
 *
 * @author Tulir Asokan
 */
public class Recipe implements ISQLTableClass {
	public static Connection db;
	public int id, yield;
	public String name, description, author, instructions, time;
	public List<RecipePart> parts;

	public Recipe(int id, String name, String description, String author, String instructions, String time, int yield) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.instructions = instructions;
		this.time = time;
		this.yield = yield;
	}

	public void write(PreparedStatement stmt, boolean writeID) throws SQLException {
		stmt.setString(1, name);
		stmt.setString(2, description);
		stmt.setString(3, author);
		stmt.setString(4, instructions);
		stmt.setString(5, time);
		stmt.setInt(6, yield);
		if (writeID) {
			stmt.setInt(7, id);
		}
	}

	public void update() {
		update(true);
	}

	public void update(boolean partsChanged) {
		try {
			PreparedStatement stmt = db.prepareStatement("UPDATE Recipe SET name=?, description=?, author=?, instructions=?, time=?, yield=? WHERE id = ?");
			write(stmt, true);
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
			PreparedStatement stmt = db.prepareStatement("INSERT INTO Recipe (name, description, author, instructions, time, yield) VALUES (?, ?, ?, ?, ?, ?)");
			write(stmt, false);
			id = ISQLTableClass.insertAndGetID(stmt);
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
		String time = rs.getString("time");
		int yield = rs.getInt("yield");
		Recipe r = new Recipe(id, name, description, author, instructions, time, yield);
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
}
