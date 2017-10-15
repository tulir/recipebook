import React, {Component} from 'react'
import './recipebook.sass'

class RecipeBook extends Component {
	render() {
		return (
			<div className="recipebook">
				<header>RecipeBook</header>

				{/* TODO list recipes */}

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
