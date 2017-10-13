package net.maunium.recipebook;

import spark.Spark;

public class RecipeBook {
	public static void main(String[] args) {
		Spark.port(8080);
		Spark.get("/hello", (req, res) -> "Hello World");
	}
}
