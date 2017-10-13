package net.maunium.recipebook;

import net.maunium.recipebook.api.IngredientAPI;
import net.maunium.recipebook.model.*;
import net.maunium.recipebook.util.JSON;
import static spark.Spark.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeBook {
	public Connection db;

	public RecipeBook() {
		db = null;
		try {
			db = DriverManager.getConnection("jdbc:sqlite:recipebook.db");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			createTables();
		} catch (SQLException e) {
			System.out.println("Failed to create tables:");
			e.printStackTrace();
			return;
		}
		Ingredient.db = db;
		Recipe.db = db;
		RecipePart.db = db;
		Cookbook.db = db;
		CookbookEntry.db = db;

		port(8080);
		staticFileLocation("/public");

		path("/api", () -> {
			path("/ingredient", () -> {
				get("/list", IngredientAPI::list, JSON.transformer());
				post("/add", IngredientAPI::add, JSON.transformer());
				put("/:id", IngredientAPI::rename, JSON.transformer());
				delete("/:id", IngredientAPI::delete, JSON.transformer());
			});
		});

		System.out.println("Running at http://localhost:8080");
	}

	public void createTables() throws SQLException {
		Statement stmt = db.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS Recipe (" +
				"id          INTEGER      PRIMARY KEY," +
				"name        VARCHAR(255) NOT NULL," +
				"author      VARCHAR(255) NOT NULL," +
				"description VARCHAR(255) NOT NULL" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS Ingredient (" +
				"id   INTEGER      PRIMARY KEY," +
				"name VARCHAR(255) NOT NULL" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS RecipePart (" +
				"ingredient  INTEGER    NOT NULL," +
				"recipe      INTEGER    NOT NULL," +
				"`order`     INTEGER    NOT NULL," +
				"amount      INTEGER    NOT NULL," +
				"unit        VARCHAR(5) NOT NULL," +
				"instruction TEXT       NOT NULL," +
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

	public static void main(String[] args) {
		new RecipeBook();
	}
}
