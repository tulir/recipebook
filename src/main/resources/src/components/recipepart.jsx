import React, { Component } from 'react'

class RecipePart extends Component {
	render() {
		return (
			<div className="recipe-part">
				{this.props.amount} {this.props.unit} of {this.props.ingredient.name}

				{this.props.instructions}
			</div>
		)
	}
}

export default RecipePart
