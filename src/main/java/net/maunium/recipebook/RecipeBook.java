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
 * @project RecipeBook
 */
public class RecipeBook {
	/**
	 * The database connection used.
	 */
	public Connection db;

	/**
	 * Create and initialize the RecipeBook.
	 */
	public RecipeBook() {
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
		port(8080);
		staticFileLocation("/webapp");
		setupSparkRoutes();

		// All done.
		System.out.println("Running at http://localhost:8080");
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
				delete("/:id", Ingredients::delete);
			});
			path("/recipe", () -> {
				get("/list", Recipes::list, JSON.transformer());
				post("/add", Recipes::add, JSON.transformer());
				get("/:id", Recipes::get, JSON.transformer());
				put("/:id", Recipes::edit, JSON.transformer());
				delete("/:id", Recipes::delete);
			});
			path("/cookbook", () -> {
				get("/list", Cookbooks::list, JSON.transformer());
				post("/add", Cookbooks::add, JSON.transformer());
				get("/:id", Cookbooks::get, JSON.transformer());
				put("/:id", Cookbooks::edit, JSON.transformer());
				delete("/:id", Cookbooks::delete);
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
	 * Main function. Calls {@link RecipeBook#RecipeBook()}
	 */
	public static void main(String[] args) {
		new RecipeBook();
	}
}
