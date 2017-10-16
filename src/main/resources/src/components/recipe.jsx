import React, { PureComponent } from 'react'
import './recipe.sass'

class Recipe extends PureComponent {
	render() {
		console.log(this.props)
		return (
			<div className="recipe">
				<span className="name">
					{this.props.name}
				</span> by <span className="author">
					{this.props.author}
				</span>
				<div className="parts">
					{this.props.parts.map(part => part.render())}
				</div>
				<span className="instructions">{this.props.instructions}</span>
			</div>
		)
	}
}

export default Recipe
