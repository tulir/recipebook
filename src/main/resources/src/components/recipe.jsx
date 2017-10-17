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
import RecipePart from "./recipepart"
import EditIcon from "../res/edit.svg"
import PropTypes from "prop-types"

class Recipe extends PureComponent {
	static contextTypes = {
		editRecipe: PropTypes.func.isRequired,
		viewRecipe: PropTypes.func.isRequired,
	}

	constructor(props, context) {
		super(props, context)

		this.edit = this.edit.bind(this)
		this.view = this.view.bind(this)
	}

	edit() {
		this.context.editRecipe(this.props.id)
	}

	view() {
		this.context.viewRecipe(this.props.id)
	}

	render() {
		if (this.props.listView) {
			return (
				<div className="recipe list-view" onClick={this.view} key={this.props.name}>
					<div className="information">
						<span className="name">
							{this.props.name}
						</span> by <span className="author">
							{this.props.author}
						</span>
						<span className="description">
							{this.props.description}
						</span>
					</div>
				</div>
			)
		}
		return (
			<div className="recipe" key={this.props.name}>
				<button onClick={this.edit} className="edit">
					<EditIcon/>
				</button>

				<div className="information">
					<span className="name">
						{this.props.name}
					</span> by <span className="author">
						{this.props.author}
					</span>
					<span className="description">
						{this.props.description}
					</span>
					<div className="parts">
						{this.props.parts.map((part, index) => <RecipePart key={index} {...part}/>)}
					</div>
					<span className="instructions">{this.props.instructions}</span>
				</div>
			</div>
		)
	}
}

export default Recipe
