import React, {Component} from 'react'
import Ingredient from './components/ingredient'
import RecipePart from './components/recipepart'
import Recipe from './components/recipe'
import './recipebook.sass'

class RecipeBook extends Component {
	constructor(props) {
		super(props)
		this.state = {
			ingredients: new Map(),
			recipes: []
		}
		this.fetchIngredients()
			.then(() => this.fetchRecipes())
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
				const recipes = []
				for (const recipe of data) {
					const parts = []
					for (const part of recipe.parts) {
						const ingredient = this.state.ingredients.get(part.ingredientID)
						part.ingredient = ingredient
						delete part.ingredientID
						parts[part.order] = new RecipePart(part)
					}
					recipe.parts = parts
					recipes.push(new Recipe(recipe))
				}
				this.setState({recipes})
			}, err => console.log("Unexpected error:", err))
	}

	render() {
		const recipes = this.state.recipes
		console.log(recipes)
		return (
			<div className="recipebook">
				<header>RecipeBook</header>

				<div className="recipes">
					{recipes}
				</div>

				<footer>
					<button onClick={this.newRecipe}>
						New recipe
					</button>
					<button onClick={this.viewIngredients}>
						View known ingredients
					</button>
				</footer>
			</div>
		)
	}

	newRecipe() {

	}

	viewIngredients() {

	}
}

export default RecipeBook
