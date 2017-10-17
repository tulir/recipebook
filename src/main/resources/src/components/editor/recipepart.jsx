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

class PartEditor extends Component {
	static contextTypes = {
		ingredients: PropTypes.object,
		deletePart: PropTypes.func,
	}

	constructor(props, context) {
		super(props, context)
		this.state = Object.assign({
			ingredient: {
				props: {
					id: 1
				}
			},
			unit: "",
			amount: 0,
			instructions: ""
		}, props)

		this.handleInputChange = this.handleInputChange.bind(this)
		this.delete = this.delete.bind(this)
	}

	handleInputChange(event) {
		const target = event.target.name
		let value = event.target.value
		if (event.target.type === "number") {
			value = +value
		}

		if (target === "ingredient") {
			this.setState({ingredient: this.context.ingredients.get(+value)})
		} else {
			this.setState({[target]: value})
		}
	}

	render() {
		return (
			<div className="part-editor">
				<input className="amount" placeholder="amount" name="amount" type="number" value={this.state.amount} onChange={this.handleInputChange}/>
				<input className="unit" placeholder="unit" name="unit" value={this.state.unit} onChange={this.handleInputChange}/>
				&nbsp;of&nbsp;
				<select className="ingredient" name="ingredient" value={this.state.ingredient.props.id} onChange={this.handleInputChange}>
					{Array.from(this.context.ingredients).map(([, ingredient]) => (
						<option key={ingredient.props.id} value={ingredient.props.id}>
							{ingredient.props.name}
						</option>
					))}
				</select>
				<br/>
				<textarea rows="3" className="instructions" placeholder="Additional instructions..." name="instructions" value={this.state.instructions} onChange={this.handleInputChange}/>

				<div className="buttons">
					<button type="button" className="delete" onClick={this.delete}>Delete</button>
					<button type="button" className="duplicate">Duplicate</button>
				</div>
			</div>
		)
	}

	delete() {
		this.context.deletePart(this)
	}
}

export default PartEditor
