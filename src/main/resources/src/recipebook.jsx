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
import React, { Component } from "react"
import PropTypes from "prop-types"
import RecipeEditor from './components/editor/recipe'
import RecipeList from './components/recipelist'
import Recipe from './components/recipe'

const
	VIEW_RECIPE_LIST = "recipe-list",
	VIEW_EDIT_RECIPE = "recipe-editor",
	VIEW_VIEW_RECIPE = "recipe-full-view",
	VIEW_INGREDIENT_LIST = "ingredient-list"

class RecipeBook extends Component {
	static childContextTypes = {
		ingredients: PropTypes.object,
		recipes: PropTypes.object,
		newRecipe: PropTypes.func,
		listIngredients : PropTypes.func,
		editRecipe: PropTypes.func,
		viewRecipe: PropTypes.func,
		saveRecipe: PropTypes.func,
	}

	getChildContext() {
		return {
			ingredients: this.state.ingredients,
			recipes: this.state.recipes,
			newRecipe: this.newRecipe,
			listIngredients : this.listIngredients ,
			editRecipe: this.editRecipe,
			viewRecipe: this.viewRecipe,
			saveRecipe: this.saveRecipe,
		}
	}

	constructor(props) {
		super(props)
		this.state = {
			ingredients: new Map(),
			recipes: new Map(),
			view: VIEW_RECIPE_LIST,
			currentRecipe: {},
		}
		this.fetchIngredients()
			.then(() => this.fetchRecipes())

		// Make sure the context functions are always called with this instance as the function context.
		for (const [key, func] of Object.entries(this.getChildContext())) {
			if (typeof(func) === "function") {
				this[key] = func.bind(this)
			}
		}
	}

	fetchIngredients() {
		return fetch("api/ingredient/list").then(response => response.json())
			.then(data => {
				const ingredients = new Map()
				for (const ingredient of data) {
					ingredients.set(ingredient.id, ingredient)
				}
				this.setState({ingredients})
			})
			.catch(err => console.log("Unexpected error:", err))
	}

	processRecipeFromServer(recipe) {
		for (const part of recipe.parts) {
			const ingredient = this.state.ingredients.get(part.ingredientID)
			part.ingredient = ingredient
			delete part.ingredientID
		}
	}

	fetchRecipes() {
		return fetch("api/recipe/list").then(response => response.json())
			.then(data => {
				const recipes = new Map()
				for (const recipe of data) {
					this.processRecipeFromServer(recipe)
					recipes.set(recipe.id, recipe)
				}
				this.setState({recipes})
			}, err => console.log("Unexpected error:", err))
	}

	render() {
		return (
			<div className="recipebook">
				<header>RecipeBook</header>

				{ this.state.view === VIEW_RECIPE_LIST ? <RecipeList recipes={this.state.recipes}/> : ""}
				{ this.state.view === VIEW_EDIT_RECIPE ? <RecipeEditor {...this.state.currentRecipe}/> : ""}
				{ this.state.view === VIEW_VIEW_RECIPE ? <Recipe {...this.state.currentRecipe}/> : ""}
				{ this.state.view === VIEW_INGREDIENT_LIST ? <RecipeList/> : ""}
			</div>
		)
	}

	newRecipe() {
		this.setState({
			view: VIEW_EDIT_RECIPE,
			currentRecipe: {},
		})
	}

	listIngredients () {
		this.setState({view: VIEW_INGREDIENT_LIST})
	}

	editRecipe(recipeID) {
		const recipe = this.state.recipes.get(recipeID)
		this.setState({
			view: VIEW_EDIT_RECIPE,
			currentRecipe: recipe,
		})
	}

	viewRecipe(recipeID) {
		const recipe = this.state.recipes.get(recipeID)
		this.setState({
			view: VIEW_VIEW_RECIPE,
			currentRecipe: recipe,
		})
	}

	saveRecipe(recipeID, newData) {
		console.log(recipeID, newData)
		let url = "api/recipe/add", method = "POST"
		if (recipeID) {
			url = `api/recipe/${recipeID}`
			method = "PUT"
		}
		fetch(url, {
			headers: {
				"Content-Type": "application/json"
			},
			method,
			body: JSON.stringify(newData)
		}).then(response => response.json())
			.then(data => {
				this.processRecipeFromServer(data)
				const recipes = this.state.recipes
				recipes.set(data.id, data)

				this.setState({recipes})
				this.viewRecipe(data.id)
			})
			.catch(err => console.log("Unexpected error:", err))
	}
}

export default RecipeBook
