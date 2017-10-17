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
import Ingredient from './components/ingredient'
import RecipeEditor from './components/editor/recipe'
import RecipeList from './components/recipelist'

class RecipeBook extends Component {
	static childContextTypes = {
		ingredients: PropTypes.object,
		recipes: PropTypes.array,
		enterRecipeEditor: PropTypes.func,
		enterIngredientList: PropTypes.func,
		editRecipe: PropTypes.func,
	}

	getChildContext() {
		return {
			ingredients: this.state.ingredients,
			recipes: this.state.recipes,
			enterRecipeEditor: this.enterRecipeEditor,
			enterIngredientList: this.enterIngredientList,
			editRecipe: this.editRecipe,
		}
	}

	constructor(props) {
		super(props)
		this.state = {
			ingredients: new Map(),
			recipes: [],
			view: "recipelist",
			recipeToEdit: {},
		}
		this.fetchIngredients()
			.then(() => this.fetchRecipes()
				.then(this.forceUpdate()))
		this.enterRecipeEditor = this.enterRecipeEditor.bind(this)
		this.enterIngredientList = this.enterIngredientList.bind(this)
		this.editRecipe = this.editRecipe.bind(this)
	}

	fetchIngredients() {
		return fetch("api/ingredient/list").then(response => response.json())
			.then(data => {
				const ingredients = new Map()
				for (const ingredient of data) {
					ingredients.set(ingredient.id, new Ingredient(ingredient))
				}
				this.setState({ingredients})
			})
			.catch(err => console.log("Unexpected error:", err))
	}

	fetchRecipes() {
		return fetch("api/recipe/list").then(response => response.json())
			.then(data => {
				for (const recipe of data) {
					for (const part of recipe.parts) {
						const ingredient = this.state.ingredients.get(part.ingredientID)
						part.ingredient = ingredient
						delete part.ingredientID
					}
				}
				this.setState({recipes: data})
			}, err => console.log("Unexpected error:", err))
	}

	render() {
		return (
			<div className="recipebook">
				<header>RecipeBook</header>

				{ this.state.view === "recipelist" ? <RecipeList recipes={this.state.recipes} enterRecipeEditor={this.enterRecipeEditor} enterIngredientList={this.enterIngredientList}/> : ""}
				{ this.state.view === "recipeeditor" ? <RecipeEditor {...this.state.recipeToEdit}/> : ""}
				{ this.state.view === "ingredientlist" ? <RecipeList/> : ""}
			</div>
		)
	}

	enterRecipeEditor() {
		this.setState({
			view: "recipeeditor",
			recipeToEdit: {},
		})
	}

	enterIngredientList() {
		this.setState({view: "ingredientlist"})
	}

	editRecipe(recipe) {
		this.setState({
			view: "recipeeditor",
			recipeToEdit: recipe.props,
		})
	}
}

export default RecipeBook
