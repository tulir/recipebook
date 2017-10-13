package net.maunium.recipebook;

import spark.Spark;

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
		Recipe.db = db;
		Ingredient.db = db;
		Spark.port(8080);
		Spark.get("/hello", (req, res) -> "Hello World");
	}

	public void createTables() throws SQLException {
		Statement stmt = db.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS Recipe (" +
				"id          INTEGER      PRIMARY KEY," +
				"name        VARCHAR(255) NOT NULL," +
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
		stmt.execute("CREATE TABLE IF NOT EXISTS RecipeBookEntry (" +
				"book   INTEGER NOT NULL," +
				"recipe INTEGER NOT NULL," +
				"FOREIGN KEY (book)   REFERENCES RecipeBook(id)," +
				"FOREIGN KEY (recipe) REFERENCES Recipe(id)" +
			")");
		stmt.execute("CREATE TABLE IF NOT EXISTS RecipeBook (" +
				"id          INTEGER PRIMARY KEY," +
				"name        VARCHAR(255) NOT NULL," +
				"description VARCHAR(255) NOT NULL" +
			")");
	}

	public static void main(String[] args) {
		new RecipeBook();
	}
}
