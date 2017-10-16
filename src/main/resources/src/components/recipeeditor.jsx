import React, { Component } from "react"
import PartEditor from "./parteditor"
import "./recipeeditor.sass"

class RecipeEditor extends Component {
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
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
	}

	render() {
		return (
			<form className="recipe-editor" onSubmit={this.save}>
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
					<textarea rows="8" name="instructions" value={this.state.instructions} onChange={this.handleInputChange}/>
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
		this.setState({
			parts: this.state.parts.concat([{}])
		})
	}

	save(event) {
		event.stopPropagation()
		// TODO implement saving
	}
}

export default RecipeEditor
