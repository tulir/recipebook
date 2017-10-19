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
import React, {Component} from "react"
import PropTypes from "prop-types"
import {Hashmux} from "hashmux"
import RecipeEditor from "./components/editor/recipe"
import RecipeList from "./components/recipelist"
import Recipe from "./components/recipe"
import ConfirmModal from "./components/confirm-modal"
import IngredientList from "./components/ingredientlist"
import BackIcon from "./res/back.svg"

const
	VIEW_RECIPE_LIST = "recipe-list",
	VIEW_EDIT_RECIPE = "recipe-editor",
	VIEW_VIEW_RECIPE = "recipe-full-view",
	VIEW_INGREDIENT_LIST = "ingredient-list"

const MAIN_VIEW = VIEW_RECIPE_LIST

class RecipeBook extends Component {
	static childContextTypes = {
		ingredients: PropTypes.object,
		recipes: PropTypes.object,
		listRecipes: PropTypes.func,
		newRecipe: PropTypes.func,
		editRecipe: PropTypes.func,
		viewRecipe: PropTypes.func,
		saveRecipe: PropTypes.func,
		deleteRecipe: PropTypes.func,
		listIngredients: PropTypes.func,
		saveIngredient: PropTypes.func,
		deleteIngredient: PropTypes.func,
		confirm: PropTypes.func,
		back: PropTypes.func,
	}

	getChildContext() {
		return {
			ingredients: this.state.ingredients,
			recipes: this.state.recipes,
			listRecipes: () => window.location.hash = "#/",
			newRecipe: (id) => window.location.hash = `#/recipe/new`,
			editRecipe: (id) => window.location.hash = `#/recipe/${id}/edit`,
			viewRecipe: (id) => window.location.hash = `#/recipe/${id}`,
			saveRecipe: this.saveRecipe.bind(this),
			deleteRecipe: this.deleteRecipe.bind(this),
			listIngredients: () => window.location.hash = "#/ingredients",
			saveIngredient: this.saveIngredient.bind(this),
			deleteIngredient: this.deleteIngredient.bind(this),
			confirm: this.confirm.bind(this),
			back: this.safeBack.bind(this),
		}
	}

	constructor(props) {
		super(props)
		this.state = {
			ingredients: new Map(),
			recipes: new Map(),
			view: MAIN_VIEW,
			currentRecipe: {},
			modal: undefined,
		}
		window.app = this

		this.router = new Hashmux()
		this.router.handle("/", () => this.listRecipes())
		this.router.handle("/ingredients", () => this.listIngredients())
		this.router.handle("/recipe/{id:[0-9]+}", ({id}) => this.viewRecipe(+id))
		this.router.handle("/recipe/{id:[0-9]+}/edit", ({id}) => this.editRecipe(+id))
		this.router.handle("/recipe/new", () => this.newRecipe())
		this.viewLog = []
		this.router.specialHandlers.prehandle = hash => {
			this.viewLog.push(hash)
			return false
		}
	}

	componentDidMount() {
		this.fetchIngredients()
			.then(() => this.fetchRecipes()
				.then(() => this.router.listen()))
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
			if (!ingredient) {
				part.ingredient = {
					name: "Unknown/Deleted Ingredient",
					id: -1,
				}
			} else {
				part.ingredient = ingredient
			}
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

	canGoBack() {
		return this.state.view !== VIEW_RECIPE_LIST
	}

	subtitle() {
		switch(this.state.view) {
			case VIEW_VIEW_RECIPE:
				return this.state.currentRecipe ? this.state.currentRecipe.name : ""
			case VIEW_EDIT_RECIPE:
				if (this.state.currentRecipe.name) {
					return `Editing ${this.state.currentRecipe.name}`
				} else {
					return "Creating Recipe"
				}
			case VIEW_INGREDIENT_LIST:
				return "Ingredients"
			case VIEW_RECIPE_LIST:
				return "Recipes"
			default:
				return ""
		}
	}

	safeBack() {
		switch(this.state.view) {
			case VIEW_VIEW_RECIPE:
			case VIEW_INGREDIENT_LIST:
				this.goBackTo("#/")
				break
			case VIEW_EDIT_RECIPE:
				if (this.state.currentRecipe.id) {
					this.goBackTo(`#/recipe/${this.state.currentRecipe.id}`)
				} else {
					this.goBackTo("#/")
				}
				break
			case MAIN_VIEW:
				// There shouldn't be a back button here.
				break
			default:
				// Unknown view, just go back
				window.history.back()
		}
	}

	/**
	 * Delete the latest history entry and go to the same URL.
	 *
	 * This is useful when we're assuming that the previous page
	 * is the page we want to go to, but we also want to make sure
	 * that the user goes where he intended to go.
	 *
	 * If the previous page is the same as the URL given, the
	 * window.location.hash assignment does nothing.
	 */
	goBackTo(url) {
		// If the view log has only one entry, history.back() might leave the app.
		if (this.viewLog.length > 1) {
			window.history.back()
		}
		window.location.hash = url
	}

	render() {
		return (
			<div className="recipebook-wrapper">
				<div className="recipebook">
					<header>
						<button onClick={() => this.safeBack()} className={this.state.view === MAIN_VIEW ? "hidden back" : "back"}>
							<BackIcon/>
						</button>
						<span className="title">
							RecipeBook
							<span className="subtitle">{this.subtitle()}</span>
						</span>
					</header>
					{this.state.view === VIEW_RECIPE_LIST ? <RecipeList recipes={this.state.recipes}/> : ""}
					{this.state.view === VIEW_EDIT_RECIPE ? <RecipeEditor {...this.state.currentRecipe}/> : ""}
					{this.state.view === VIEW_VIEW_RECIPE ? <Recipe {...this.state.currentRecipe}/> : ""}
					{this.state.view === VIEW_INGREDIENT_LIST ? <IngredientList ingredients={this.state.ingredients}/> : ""}
				</div>

				{this.state.modal ? this.state.modal.render() : ""}
			</div>
		)
	}

	confirm(message) {
		const promise = new Promise((resolve, reject) => {
			const modal = new ConfirmModal({
				content: <div className="message">{message}</div>,
				type: "",
				resolve,
				reject,
			})
			this.setState({modal})
		})
		promise.then(() => {this.hideModal()})
			.catch(() => {this.hideModal()})
		return promise
	}

	hideModal() {
		this.setState({
			modal: undefined
		})
	}

	newRecipe() {
		this.setState({
			view: VIEW_EDIT_RECIPE,
			currentRecipe: {},
		})
	}

	listRecipes() {
		this.setState({view: VIEW_RECIPE_LIST})
	}

	listIngredients() {
		this.setState({view: VIEW_INGREDIENT_LIST})
	}

	saveIngredient(ingredientID, newData) {
		let url = "api/ingredient/add", method = "POST"
		if (ingredientID) {
			url = `api/ingredient/${ingredientID}`
			method = "PUT"
		}
		return fetch(url, {
			headers: {
				"Content-Type": "application/json"
			},
			method,
			body: JSON.stringify(newData)
		}).then(response => response.json())
			.then(data => {
				const ingredients = this.state.ingredients
				ingredients.set(data.id, data)
				this.setState({ingredients})
				return data
			})
			.catch(err => console.log("Unexpected error:", err))
	}

	deleteIngredient(ingredientID) {
		return fetch(`api/ingredient/${ingredientID}`, {
			method: "DELETE"
		}).then(() => {
			const ingredients = this.state.ingredients
			ingredients.delete(ingredientID)
			this.setState({ingredients})
		}).catch(err => console.log("Unexpected error:", err))
	}

	editRecipe(recipeID) {
		const recipe = this.state.recipes.get(recipeID)
		if (recipe) {
			this.setState({
				view: VIEW_EDIT_RECIPE,
				currentRecipe: recipe,
			})
		}
	}

	viewRecipe(recipeID) {
		const recipe = this.state.recipes.get(recipeID)
		if (recipe) {
			this.setState({
				view: VIEW_VIEW_RECIPE,
				currentRecipe: recipe,
			})
		}
	}

	deleteRecipe(recipeID) {
		return fetch(`api/recipe/${recipeID}`, {
			method: "DELETE"
		}).then(() => {
			const recipes = this.state.recipes
			recipes.delete(recipeID)
			this.goBackTo(`#/`)
			this.setState({recipes})
		}).catch(err => console.log("Unexpected error:", err))
	}

	saveRecipe(recipeID, newData) {
		let url = "api/recipe/add", method = "POST"
		if (recipeID) {
			url = `api/recipe/${recipeID}`
			method = "PUT"
		}
		return fetch(url, {
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
				this.goBackTo(`#/recipe/${data.id}`)
				return data
			})
			.catch(err => console.log("Unexpected error:", err))
	}
}

export default RecipeBook
