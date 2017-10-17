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
		deletePart: PropTypes.func
	}

	getChildContext() {
		return {
			deletePart: this.deletePart
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
			this.state = {name, author, description, instructions, parts}
		}
		this.handleInputChange = this.handleInputChange.bind(this)
		this.addPart = this.addPart.bind(this)
		this.deletePart = this.deletePart.bind(this)
		this.save = this.save.bind(this)
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
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
					{this.state.parts.map((part, index) => <PartEditor key={index} {...part}/>)}
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
			ingredient: {props: {id: 1}}
		}
		this.setState({
			parts: this.state.parts.concat([blankPart])
		})
	}

	deletePart(ptd) {
		const parts = this.state.parts
		ptd = ptd.props
		for (const [index, part] of Object.entries(parts)) {
			if (part.amount === ptd.amount
				&& part.unit === ptd.unit
				&& part.instructions === ptd.instructions
				&& part.ingredient.props.id === ptd.ingredient.props.id) {
				delete parts[index]
				this.setState({parts})
				return
			}
		}
	}

	save(event) {
		event.preventDefault()
		alert("Saving is not fully implemented and completely broken")
		
		const state = Object.assign({}, this.state)
		// De-componentify ingredients
		for (const part of state.parts) {
			part.ingredientID = part.ingredient.props.id
			delete part.ingredient
		}

		let url = "api/recipe/add", method = "POST"
		if (this.id) {
			url = `api/recipe/${this.id}`
			method = "PUT"
		}
		fetch(url, {
			headers: {
				"Content-Type": "application/json"
			},
			method,
			body: JSON.stringify(state)
		}).then(response => response.json())
			.then(data => {
				console.log(data)
			})
			.catch(err => console.log("Unexpected error:", err))
	}
}

export default RecipeEditor
