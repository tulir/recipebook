import React, {Component} from "react"
import PartEditor from "./parteditor"
import "./recipeeditor.sass"
import PropTypes from "prop-types"

class RecipeEditor extends Component {
	static childContextTypes = {
		deletePart: PropTypes.func
	}

	getChildContext() {
		return {
			deletePart: this.deletePart
		}
	}

	constructor(props) {
		super(props)
		this.state = {parts: []}
		if (props) {
			let {name, author, description, instructions, parts} = props
			if (!parts) {
				parts = []
			}
			this.state = {name, author, description, instructions, parts}
		}
		this.handleInputChange = this.handleInputChange.bind(this)
		this.addPart = this.addPart.bind(this)
		this.deletePart = this.deletePart.bind(this)
		this.save = this.save.bind(this)
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
	}

	render() {
		return (
			<form action="#" className="recipe-editor" onSubmit={this.save}>
				<div className="field">
					<label htmlFor="name">Name</label>
					<input name="name" value={this.state.name} onChange={this.handleInputChange}/>
				</div>
				<div className="field">
					<label htmlFor="author">Author</label>
					<input name="author" value={this.state.author} onChange={this.handleInputChange}/>
				</div>
				<div className="field">
					<label htmlFor="description">Description</label>
					<input name="description" value={this.state.description} onChange={this.handleInputChange}/>
				</div>
				<div className="field">
					<label htmlFor="instructions">Instructions</label>
					<textarea rows="8" name="instructions" value={this.state.instructions}
							  onChange={this.handleInputChange}/>
				</div>

				<div className="part-editors">
					{this.state.parts.map((part, index) => <PartEditor key={index} {...part}/>)}
				</div>

				<button type="button" className="add-part" onClick={this.addPart}>
					Add
				</button>
				<button type="submit">
					Save
				</button>
			</form>
		)
	}

	addPart() {
		const blankPart = {
			ingredient: {
				props: {
					id: 1
				}
			}
		}
		this.setState({
			parts: this.state.parts.concat([blankPart])
		})
	}

	deletePart(ptd) {
		const parts = this.state.parts
		ptd = ptd.props
		for (const [index, part] of Object.entries(parts)) {
			if (part.amount === ptd.amount
				&& part.unit === ptd.unit
				&& part.instructions === ptd.instructions
				&& part.ingredient.props.id === ptd.ingredient.props.id) {
				delete parts[index]
				this.setState({parts})
				return
			}
		}
	}

	save(event) {
		event.stopPropagation()
		// TODO implement saving
	}
}

export default RecipeEditor
