import React from "react"
import ReactDOM from "react-dom"
import "./style/index.sass"
import RecipeBook from "./recipebook"

// A helper function to use Maps easily within JSX.
// eslint-disable-next-line
Map.prototype.map = function (...args) {
	return [...this.values()].map(...args)
}

// Render app
ReactDOM.render(<RecipeBook/>, document.getElementById('root'))
