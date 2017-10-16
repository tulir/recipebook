import React, { PureComponent } from "react"

class RecipePart extends PureComponent {
	render() {
		return (
			<div className="recipe-part" key={this.props.index}>
				<span className="amount">
					{this.props.amount}
				</span> <span className="unit">
					{this.props.unit}
				</span> of <span className="ingredient">
					{this.props.ingredient.render()}
				</span>
				<span className="instructions">{this.props.instructions}</span>
			</div>
		)
	}
}

export default RecipePart
