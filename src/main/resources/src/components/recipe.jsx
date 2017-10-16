import React, { PureComponent } from "react"
import "./recipe.sass"
import RecipePart from "./recipepart"
import EditIcon from "../res/edit.svg"
import PropTypes from "prop-types"

class Recipe extends PureComponent {
	static contextTypes = {
		editRecipe: PropTypes.func.isRequired,
	}

	constructor(props, context) {
		super(props, context)

		this.edit = this.edit.bind(this)
	}

	edit() {
		this.context.editRecipe(this)
	}

	render() {
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
