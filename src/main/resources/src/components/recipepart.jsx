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
import React, { PureComponent } from "react"
import Ingredient from "./ingredient"

class RecipePart extends PureComponent {
	render() {
		return (
			<div className="recipe-part" key={this.props.index}>
				<span className="amount">
					{this.props.amount}
				</span> <span className="unit">
					{this.props.unit}
				</span> of <Ingredient {...this.props.ingredient}/>
				<span className="instructions">{this.props.instructions}</span>
			</div>
		)
	}
}

export default RecipePart
