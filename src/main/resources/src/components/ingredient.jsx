import React, { Component } from 'react'

class Ingredient extends Component {
	render() {
		return (
			<div className="ingredient">
				Ingredient #{this.props.id}: {this.props.name}
			</div>
		)
	}
}

export default Ingredient
