// RecipeBook - An Introduction to Databases exercise project with Java Spark and React.
// Copyright (C) 2017  Maunium

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
import React, {PureComponent} from "react"
import DeleteIcon from "../res/delete.svg"
import EditIcon from "../res/edit.svg"
import SaveIcon from "../res/save.svg"
import PropTypes from "prop-types"

class Ingredient extends PureComponent {
	static contextTypes = {
		deleteIngredient: PropTypes.func.isRequired,
		saveIngredient: PropTypes.func.isRequired,
		stopEditingIngredient: PropTypes.func,
	}

	constructor(props, context) {
		super(props, context)

		this.state = Object.assign({editing: false, name: ""}, props)

		this.edit = this.edit.bind(this)
		this.save = this.save.bind(this)
		this.delete = this.delete.bind(this)
		this.cancelEdit = this.cancelEdit.bind(this)
		this.handleInputChange = this.handleInputChange.bind(this)
		this.handleEditKeyPress = this.handleEditKeyPress.bind(this)
	}

	componentDidMount() {
		if (this.props.editing && this.state.editing) {
			this.refs.nameInput.focus()
		}
	}

	edit() {
		this.setState({editing: true}, () => this.refs.nameInput.focus())
	}

	save() {
		if (this.state.name.length === 0) {
			this.cancelEdit()
			return
		}
		this.setState({editing: false})
		this.context.saveIngredient(this.props.id, Object.assign({}, this.state))
	}

	componentWillReceiveProps(_, newProps) {
		if (newProps.name) {
			this.setState({name: newProps.name})
		}
	}

	delete() {
		const confirmation = window.confirm(`Are you sure you want to delete ${this.state.name}?`)
		if (confirmation) {
			this.context.deleteIngredient(this.props.id)
		}
	}

	cancelEdit() {
		this.setState({editing: false, name: this.props.name})
		if (this.context.stopEditingIngredient) {
			this.context.stopEditingIngredient(this.props.id)
		}
	}

	handleInputChange(event) {
		this.setState({[event.target.name]: event.target.value})
	}

	handleEditKeyPress(event) {
		if (event.key === "Enter") {
			this.save()
		} else if (event.key === "Escape") {
			this.cancelEdit()
		}
	}

	render() {
		if (this.props.listView) {
			return (
				<div className="ingredient">
					<div className="info">
						<span className="id">
							{this.props.id}
						</span>
						<span className="name">
							{!this.state.editing ? this.state.name :
								<input ref="nameInput" name="name" value={this.state.name} onBlur={this.cancelEdit}
									   onKeyUp={this.handleEditKeyPress} onChange={this.handleInputChange}/>
							}
						</span>
					</div>

					<div className="buttons">
						{this.state.editing ? (
							<button onClick={this.save} className="save">
								<SaveIcon/>
							</button>
						) : (
							<button onClick={this.edit} className="edit">
								<EditIcon/>
							</button>
						)}

						<button onClick={this.delete} className="delete">
							<DeleteIcon/>
						</button>
					</div>
				</div>
			)
		}
		return (
			<span className="ingredient">
				{this.props.name}
			</span>
		)
	}
}

export default Ingredient
