import React, { Component } from "react"
import PropTypes from "prop-types"

class PartEditor extends Component {
	static contextTypes = {
		ingredients: PropTypes.object,
	}

	constructor(props, context) {
		super(props, context)
		this.state = {
			ingredient: {
				props: {
					id: 1
				}
			}
		}
		if (props) {
			this.state = props
		}

		this.handleInputChange = this.handleInputChange.bind(this)
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
	}

	render() {
		return (
			<div className="part-editor">
				<label htmlFor="name">Name</label>
				<select name="name" value={this.state.ingredient.props.id} onChange={this.handleInputChange}>
					{Array.from(this.context.ingredients).map(([, ingredient]) => (
						<option key={ingredient.props.id} value={ingredient.props.id}>
							{ingredient.props.name}
						</option>
					))}
				</select>

				<label htmlFor="amount">Amount</label>
				<input name="amount" type="number" value={this.state.amount} onChange={this.handleInputChange}/>
				<label htmlFor="unit">Unit</label>
				<input name="unit" value={this.state.unit} onChange={this.handleInputChange}/>
				<label htmlFor="instructions">Instructions</label>
				<textarea name="instructions" value={this.state.instructions} onChange={this.handleInputChange}/>
			</div>
		)
	}
}

export default PartEditor
