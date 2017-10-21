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
import React, {PureComponent} from "react"
import ReactMarkdown from "react-markdown"
import RecipePart from "./recipepart"
import EditIcon from "../res/edit.svg"
import DeleteIcon from "../res/delete.svg"
import PropTypes from "prop-types"

class Recipe extends PureComponent {
	static contextTypes = {
		editRecipe: PropTypes.func.isRequired,
		viewRecipe: PropTypes.func.isRequired,
		deleteRecipe: PropTypes.func.isRequired,
		confirm: PropTypes.func.isRequired,
	}

	constructor(props, context) {
		super(props, context)

		this.edit = this.edit.bind(this)
		this.view = this.view.bind(this)
		this.delete = this.delete.bind(this)
	}

	edit() {
		this.context.editRecipe(this.props.id)
	}

	view() {
		this.context.viewRecipe(this.props.id)
	}

	delete() {
		this.context.confirm(`Are you sure you want to delete ${this.props.name}?`)
			.then(() => this.context.deleteRecipe(this.props.id), () => {})
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
						<div className="description">
							{this.props.description}
						</div>
					</div>
				</div>
			)
		}
		return (
			<div className="recipe standalone" key={this.props.name}>
				<div className="buttons">
					<button onClick={this.edit} className="edit">
						<EditIcon/>
					</button>
					<button onClick={this.delete} className="delete">
						<DeleteIcon/>
					</button>
				</div>

				<div className="information">
					<span className="name">
						{this.props.name}
					</span> by <span className="author">
						{this.props.author}
					</span>

					<div className="description">
						{this.props.description}
					</div>

					<span className="time">
						Takes about {this.props.time}
					</span> - <span className="yield">
						Yields {this.props.yield} portions
					</span>
					
					<div className="parts">
						{this.props.parts.map((part, index) => <RecipePart key={index} {...part}/>)}
					</div>
					<span className="instructions">
						<ReactMarkdown source={this.props.instructions}/>
					</span>
				</div>
			</div>
		)
	}
}

export default Recipe
