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
import React, { PureComponent } from "react"
import Recipe from "./recipe"
import PropTypes from "prop-types"

class RecipeList extends PureComponent {
	static contextTypes = {
		enterIngredientList: PropTypes.func.isRequired,
		newRecipe: PropTypes.func.isRequired,
	}

	render() {
		return (
			<div>
				<div className="recipes recipebook-list">
					{this.props.recipes.map(recipe => <Recipe key={recipe.name} listView={true} {...recipe}/>)}
				</div>
				<footer>
					<button onClick={this.context.newRecipe}>
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
