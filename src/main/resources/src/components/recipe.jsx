import React, { Component } from 'react'
import './recipe.sass'

class Recipe extends Component {
	render() {
		return (
			<div className="recipe">
				<span className="name">{this.props.name}</span>
				<span className="author">{this.props.author}</span>
				<div className="parts">
					{this.props.parts}
				</div>
				<span className="instructions">{this.props.instructions}</span>
			</div>
		)
	}
}

export default Recipe
