from flask import Flask
import sqlite3

conn = sqlite3.connect('recipe.db')

c = conn.cursor()

c.execute("""CREATE TABLE IF NOT EXISTS Recipe (
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
)""")

c.execute("""CREATE TABLE IF NOT EXISTS Ingredient (
    id   INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL
)""")

c.execute("""CREATE TABLE IF NOT EXISTS RecipePart (
    ingredient  INTEGER NOT NULL,
    recipe      INTEGER NOT NULL,
    `order`     INTEGER NOT NULL,
    amount      INTEGER NOT NULL,
    unit        VARCHAR(5),
    instruction TEXT,

    FOREIGN KEY (ingredient) REFERENCES Ingredient(id),
    FOREIGN KEY (recipe)     REFERENCES Recipe(id)
)""")

app = Flask("tikape")
