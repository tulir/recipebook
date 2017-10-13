package net.maunium.recipebook;

import spark.Spark;

public class RecipeBook {
    public static void main(String[] args) {
        Spark.get("/hello", (req, res) -> "Hello World");
    }
}
