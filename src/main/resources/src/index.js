import React from "react"
import ReactDOM from "react-dom"
import "./style/index.sass"
import RecipeBook from "./recipebook"
import registerServiceWorker from "./registerServiceWorker"

ReactDOM.render(<RecipeBook/>, document.getElementById('root'))
registerServiceWorker()
