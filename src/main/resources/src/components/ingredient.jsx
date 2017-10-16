import React, { PureComponent } from 'react'

class Ingredient extends PureComponent {
	render() {
		return (
			<div className="ingredient">
				Ingredient #{this.props.id}: {this.props.name}
			</div>
		)
	}

	renderName() {
		return this.props.name
	}
}

export default Ingredient
