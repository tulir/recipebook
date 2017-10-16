import React, { Component } from "react"
import PropTypes from "prop-types"
import "./parteditor.sass"

class PartEditor extends Component {
	static contextTypes = {
		ingredients: PropTypes.object,
		deletePart: PropTypes.func,
	}

	constructor(props, context) {
		super(props, context)
		this.state = Object.assign({
			ingredient: {
				props: {
					id: 1
				}
			},
			unit: "",
			amount: 0,
			instructions: ""
		}, props)

		this.handleInputChange = this.handleInputChange.bind(this)
		this.delete = this.delete.bind(this)
	}

	handleInputChange(event) {
		const target = event.target.name
		let value = event.target.value
		if (event.target.type === "number") {
			value = +value
		}

		if (target === "ingredient") {
			this.setState({ingredient: this.context.ingredients.get(+value)})
		} else {
			this.setState({[target]: value})
		}
	}

	render() {
		return (
			<div className="part-editor">
				<input className="amount" placeholder="amount" name="amount" type="number" value={this.state.amount} onChange={this.handleInputChange}/>
				<input className="unit" placeholder="unit" name="unit" value={this.state.unit} onChange={this.handleInputChange}/>
				&nbsp;of&nbsp;
				<select className="ingredient" name="ingredient" value={this.state.ingredient.props.id} onChange={this.handleInputChange}>
					{Array.from(this.context.ingredients).map(([, ingredient]) => (
						<option key={ingredient.props.id} value={ingredient.props.id}>
							{ingredient.props.name}
						</option>
					))}
				</select>
				<br/>
				<textarea rows="3" className="instructions" placeholder="Additional instructions..." name="instructions" value={this.state.instructions} onChange={this.handleInputChange}/>

				<button type="button" className="delete" onClick={this.delete}>Delete</button>
				<button type="button" className="duplicate">Duplicate</button>
			</div>
		)
	}

	delete() {
		this.context.deletePart(this)
	}
}

export default PartEditor
