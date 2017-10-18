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
import PartEditor from "./recipepart"
import PropTypes from "prop-types"

class RecipeEditor extends Component {
	static childContextTypes = {
		deletePart: PropTypes.func,
		duplicatePart: PropTypes.func,
		childInputChange: PropTypes.func,
	}
	static contextTypes = {
		saveRecipe: PropTypes.func.isRequired,
		saveIngredient: PropTypes.func.isRequired,
		ingredients: PropTypes.object.isRequired,
	}

	getChildContext() {
		return {
			deletePart: this.deletePart,
			duplicatePart: this.duplicatePart,
			childInputChange: this.childInputChange,
		}
	}

	constructor(props) {
		super(props)
		this.state = {parts: []}
		if (props) {
			let {name, author, description, instructions, parts} = props
			if (!parts) {
				parts = []
			}
			for (const part of parts) {
				if (part.ingredient) {
					part.ingredientName = part.ingredient.name
				}
				delete part.ingredient
			}
			this.state = {name, author, description, instructions, parts}
		}
		this.handleInputChange = this.handleInputChange.bind(this)
		this.childInputChange = this.childInputChange.bind(this)
		this.addPart = this.addPart.bind(this)
		this.deletePart = this.deletePart.bind(this)
		this.duplicatePart = this.duplicatePart.bind(this)
		this.save = this.save.bind(this)
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
	}

	childInputChange(index, state) {
		const parts = this.state.parts
		parts[index] = state
		this.setState({parts})
	}

	render() {
		return (
			<form action="#" className="recipe-editor" onSubmit={this.save}>
				<div className="name field">
					<label htmlFor="name">Name</label>
					<input name="name" value={this.state.name} onChange={this.handleInputChange}/>
				</div>
				<div className="author field">
					<label htmlFor="author">Author</label>
					<input name="author" value={this.state.author} onChange={this.handleInputChange}/>
				</div>
				<div className="description field">
					<label htmlFor="description">Description</label>
					<input name="description" value={this.state.description} onChange={this.handleInputChange}/>
				</div>
				<div className="instructions field">
					<label htmlFor="instructions">Instructions</label>
					<textarea rows="8" name="instructions" value={this.state.instructions}
							  onChange={this.handleInputChange}/>
				</div>

				<div className="part-editors">
					<datalist id="ingredients">
						{this.context.ingredients.map(ingredient =>
							<option key={ingredient.id} value={ingredient.name}/>
						)}
					</datalist>
					{this.state.parts.map((part, index) => <PartEditor index={index} key={index} {...part}/>)}
				</div>

				<div className="buttons">
					<button type="button" className="add-part" onClick={this.addPart}>
						New Ingredient
					</button>
					<button type="submit">
						Save Recipe
					</button>
				</div>
			</form>
		)
	}

	addPart() {
		const blankPart = {
			// Preset the ingredient data so that we can expect it to always exist.
			ingredientName: ""
		}
		this.setState({
			parts: this.state.parts.concat([blankPart])
		})
	}

	deletePart(index) {
		const parts = this.state.parts
		delete parts[index]
		this.setState({parts})
	}

	duplicatePart(index) {
		const parts = this.state.parts
		parts.push(parts[index])
		this.setState({parts})
	}

	save(event) {
		event.preventDefault()
		const state = Object.assign({}, this.state)

		// Delete empty slots
		for (let i = 0; i < state.parts.length; i++) {
			if (!state.parts[i]) {
				state.parts.splice(i, 1)
				i--
			}
		}

		const createdIngredients = []

		// Remove ingredient names
		for (const part of state.parts) {
			const partIngredientName = part.ingredientName.trim().toLowerCase()

			// Try to find ingredients with matching names
			const ingredientID = this.context.ingredients.find(ingredient =>
				ingredient.name.trim().toLowerCase() === partIngredientName)

			if (!ingredientID) {
				// Ingredient not found 3:
				createdIngredients.push(
					// Send ingredient creation request
					this.context.saveIngredient(undefined, {
						name: part.ingredientName.trim(),
					}).then(data => 
						// Once it finishes, store the ID of the newly created ingredient in the RecipePart.
						part.ingredientID = data.id)
				)
			} else {
				// Ingredient found. Just store the ID in the RecipePart.
				part.ingredientID = ingredientID
			}
			// Delete the temporary name storage field.
			delete part.ingredientName
		}

		if (createdIngredients.length !== 0) {
			// We sent some ingredient creation requests, so we need to wait for them.
			Promise.all(createdIngredients)
				.then(() =>
					// Now that all new ingredients have been created and the IDs have been stored in their respective
					// RecipeParts, we can save the recipe.
					this.context.saveRecipe(this.props.id, state))
		} else {
			// No ingredient creation requests have been sent, so just save the recipe.
			this.context.saveRecipe(this.props.id, state)
		}
	}
}

export default RecipeEditor
