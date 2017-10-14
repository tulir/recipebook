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
 * Ingredient contains information about a single ingredient.
 *
 * @author Tulir Asokan
 */
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
