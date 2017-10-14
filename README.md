# RecipeBook
A recipe system that uses Java Spark and React.

## Structure
```
src
├───main
│   ├───java                        // Backend
│   │   └───net/maunium/recipebook
│   │       │   RecipeBook.java     // Main
│   │       ├───api                 // API request handlers
│   │       ├───model               // Data models
│   │       └───util                // Utilities
│   │
│   └───resources                   // Frontend
│       ├───webapp                  // Frontend build directory
│       ├───src                     // JS/CSS
│       └───public                  // HTML
|
└───target                          // Backend build directory
```

## Running

### Production
Run `mvn package` to compile and package code into `./target/recipebook-1.0.jar`.
Then simply run the jar and open the web server at localhost:8080

### Development
Install backend dependencies with Maven, then start the server with your IDE.
It is recommended to use IntelliJ IDEA for development.

If you intend to develop the frontend, you should use `npm run start`
in `src/main/resources` and connect to the server provided by the start script
(probably [localhost:3000](http://localhost:3000))

If you're only developing the backend, just compile the frontend
using `npm run build` and connect to the server provided by the backend
(probably [localhost:8080](http://localhost:8080))

