import React, { Component } from "react"
import PropTypes from "prop-types"
import Ingredient from './components/ingredient'
import RecipeEditor from './components/recipeeditor'
import RecipeList from './components/recipelist'
import './recipebook.sass'

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
