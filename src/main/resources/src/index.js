import React from "react"
import ReactDOM from "react-dom"
import "./index.sass"
import RecipeBook from "./recipebook"
import registerServiceWorker from "./registerServiceWorker"

ReactDOM.render(<RecipeBook/>, document.getElementById('root'))
registerServiceWorker()
