import React, { Component } from "react"
import PropTypes from "prop-types"
import "./parteditor.sass"

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
				<input className="amount" placeholder="amount (e.g. 1)" name="amount" type="number" value={this.state.amount} onChange={this.handleInputChange}/>
				<input className="unit" placeholder="unit (e.g. dl)" name="unit" value={this.state.unit} onChange={this.handleInputChange}/>
				&nbsp;of&nbsp;
				<select className="name" name="name" value={this.state.ingredient.props.id} onChange={this.handleInputChange}>
					{Array.from(this.context.ingredients).map(([, ingredient]) => (
						<option key={ingredient.props.id} value={ingredient.props.id}>
							{ingredient.props.name}
						</option>
					))}
				</select>
				<br/>
				<textarea rows="3" className="instructions" placeholder="Additional instructions..." name="instructions" value={this.state.instructions} onChange={this.handleInputChange}/>

				<button type="button" className="delete">Delete</button>
				<button type="button" className="duplicate">Duplicate</button>
			</div>
		)
	}
}

export default PartEditor
