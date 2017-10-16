import React, { PureComponent } from "react"
import Recipe from "./recipe"
import PropTypes from "prop-types"

class RecipeList extends PureComponent {
	static contextTypes = {
		enterIngredientList: PropTypes.func.isRequired,
		enterRecipeEditor: PropTypes.func.isRequired,
	}

	render() {
		return (
			<div>
				<div className="recipes recipebook-list">
					{this.props.recipes.map(recipe => <Recipe key={recipe.name} {...recipe}/>)}
				</div>
				<footer>
					<button onClick={this.context.enterRecipeEditor}>
						New recipe
					</button>
					<button onClick={this.context.enterIngredientList}>
						View known ingredients
					</button>
				</footer>
			</div>
		)
	}
}

export default RecipeList
