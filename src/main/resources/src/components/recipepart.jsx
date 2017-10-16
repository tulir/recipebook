import React, { PureComponent } from 'react'

class RecipePart extends PureComponent {
	render() {
		return (
			<div className="recipe-part">
				<span className="amount">
					{this.props.amount}
				</span> <span className="unit">
					{this.props.unit}
				</span> of <span className="ingredient">
					{this.props.ingredient.renderName()}
				</span>
				<span className="instructions">{this.props.instructions}</span>
			</div>
		)
	}
}

export default RecipePart
