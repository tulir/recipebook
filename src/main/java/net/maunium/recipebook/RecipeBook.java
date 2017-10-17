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

package net.maunium.recipebook;

import net.maunium.recipebook.api.*;
import net.maunium.recipebook.model.*;
import net.maunium.recipebook.util.JSON;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.*;

/**
 * RecipeBook is the main class of this program.
 * It creates the database connection and sets up Spark.
 *
 * @author Tulir Asokan
 */
public class RecipeBook {
	/**
	 * The database connection to use.
	 */
	public Connection db;

	/**
	 * Create and initialize the RecipeBook.
	 */
	public RecipeBook(String ip, int port) {
		db = null;
		try {
			db = DriverManager.getConnection("jdbc:sqlite:recipebook.db");
		} catch (SQLException e) {
			System.out.println("[Fatal] Failed to connect to database:");
			e.printStackTrace();
			return;
		}

		try {
			createTables();
		} catch (SQLException e) {
			System.out.println("[Fatal] Failed to create tables:");
			e.printStackTrace();
			return;
		}

		// Share database object to model classes.
		Ingredient.db = db;
		Recipe.db = db;
		RecipePart.db = db;
		Cookbook.db = db;
		CookbookEntry.db = db;

		// Set up Spark.
		ipAddress(ip);
		port(port);
		staticFileLocation("/webapp");
		setupSparkRoutes();

		// All done.
		System.out.printf("Running at http://%s:%d\n", ip, port);
	}

	/**
	 * Set up all Spark API endpoints.
	 */
	public void setupSparkRoutes() {
		path("/api", () -> {
			path("/ingredient", () -> {
				get("/list", Ingredients::list, JSON.transformer());
				post("/add", Ingredients::add, JSON.transformer());
				put("/:id", Ingredients::rename, JSON.transformer());
				delete("/:id", Ingredients::delete, JSON.transformer());
			});
			path("/recipe", () -> {
				get("/list", Recipes::list, JSON.transformer());
				post("/add", Recipes::add, JSON.transformer());
				get("/:id", Recipes::get, JSON.transformer());
				put("/:id", Recipes::edit, JSON.transformer());
				delete("/:id", Recipes::delete, JSON.transformer());
			});
			path("/cookbook", () -> {
				get("/list", Cookbooks::list, JSON.transformer());
				post("/add", Cookbooks::add, JSON.transformer());
				get("/:id", Cookbooks::get, JSON.transformer());
				put("/:id", Cookbooks::edit, JSON.transformer());
				delete("/:id", Cookbooks::delete, JSON.transformer());
			});
		});
	}

	/**
	 * Create all necessary database tables.
	 *
	 * @throws SQLException If a CREATE TABLE statement execution fails.
	 */
	public void createTables() throws SQLException {
		Statement stmt = db.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS Recipe (" +
				"id           INTEGER      PRIMARY KEY," +
				"name         VARCHAR(255) NOT NULL," +
				"author       VARCHAR(255) NOT NULL," +
				"description  VARCHAR(255) NOT NULL," +
				"instructions TEXT         NOT NULL" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS Ingredient (" +
				"id   INTEGER      PRIMARY KEY," +
				"name VARCHAR(255) NOT NULL" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS RecipePart (" +
				"ingredient   INTEGER      NOT NULL," +
				"recipe       INTEGER      NOT NULL," +
				"`order`      INTEGER      NOT NULL," +
				"amount       INTEGER      NOT NULL," +
				"unit         VARCHAR(5)   NOT NULL," +
				"instructions VARCHAR(255)," +
				"FOREIGN KEY (ingredient) REFERENCES Ingredient(id)," +
				"FOREIGN KEY (recipe)     REFERENCES Recipe(id)" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS CookbookEntry (" +
				"book   INTEGER NOT NULL," +
				"recipe INTEGER NOT NULL," +
				"FOREIGN KEY (book)   REFERENCES Cookbook(id)," +
				"FOREIGN KEY (recipe) REFERENCES Recipe(id)" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS Cookbook (" +
				"id          INTEGER      PRIMARY KEY," +
				"name        VARCHAR(255) NOT NULL," +
				"author      VARCHAR(255) NOT NULL," +
				"description VARCHAR(255) NOT NULL" +
			")");
	}

	/**
	 * Main function. Calls {@link RecipeBook#RecipeBook(String, int)}
	 */
	public static void main(String[] args) {
		String ip = "localhost";
		int port = 29314;
		if (args.length > 0) {
			ip = args[0];
			if (args.length > 1) {
				port = Integer.parseInt(args[1]);
			}
		}
		new RecipeBook(ip, port);
	}
}
