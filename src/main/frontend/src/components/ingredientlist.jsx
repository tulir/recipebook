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
import Ingredient from "./ingredient"
import PropTypes from "prop-types"

class IngredientList extends Component {
	static childContextTypes = {
		saveIngredient: PropTypes.func,
		stopEditingIngredient: PropTypes.func,
	}

	getChildContext() {
		return {
			saveIngredient: this.saveIngredient,
			stopEditingIngredient: this.stopEditingIngredient,
		}
	}

	static contextTypes = {
		saveIngredient: PropTypes.func.isRequired,
	}

	constructor(props, context) {
		super(props, context)
		this.state = {
			addingIngredient: false,
		}
		this.newIngredient = this.newIngredient.bind(this)
		this.saveIngredient = this.saveIngredient.bind(this)
		this.stopEditingIngredient = this.stopEditingIngredient.bind(this)
	}

	render() {
		return (
			<div>
				<div className="ingredients recipebook-list">
					{this.props.ingredients.map(ingredient => <Ingredient key={ingredient.id} listView={true} {...ingredient}/>)}
					{this.state.addingIngredient ? <Ingredient listView={true} id="n" editing={true}/> : ""}
				</div>
				<footer>
					<button onClick={this.newIngredient}>
						New ingredient
					</button>
				</footer>
			</div>
		)
	}

	saveIngredient(id, newData) {
		if (id === "n") {
			delete newData.id
			this.setState({addingIngredient: false})
			this.context.saveIngredient(undefined, newData)
		} else {
			this.context.saveIngredient(id, newData)
		}
	}

	stopEditingIngredient(id) {
		if (id === "n") {
			this.setState({addingIngredient: false})
		}
	}

	newIngredient() {
		this.setState({addingIngredient: true})
	}
}

export default IngredientList
