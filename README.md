# RecipeBook
An Introduction to Databases exercise project with Java Spark and React

## Running

### Production
Run `mvn clean compile package` to compile and package everything into `./target/recipebook-<version>-jar-with-dependencies.jar`.
Then, simply run the jar and open the web server at [localhost:29314](http://localhost:29314). You can change the IP and port by providing them as command-line arguments (IP first, then port).

A precompiled jar is available in [releases](https://github.com/tulir/recipebook/releases)

### Development
Install backend dependencies with Maven, then start the server with your IDE.
It is recommended to use IntelliJ IDEA for development.

If you intend to develop the frontend, you should use `npm run start`
in `src/main/resources` and connect to the server provided by the start script
(probably [localhost:3000](http://localhost:3000))

If you're only developing the backend, just compile the frontend
using `npm run build` and connect to the server provided by the backend
(probably [localhost:29314](http://localhost:29314))


## Structure

### Database
![Database schema](https://img.mau.lu/QkudU.png)

### Files
```
RecipeBook
│   recipebook.db // Default location for database
├───target        // Backend build directory (ignored in Git)
└───src/main
    ├───java                  
    │   └───net/maunium/recipebook // Backend
    │       │   RecipeBook.java    // Main
    │       ├───api                // API request handlers
    │       ├───model              // Data models
    │       └───util               // Utilities
    │
    └───frontend                   // Frontend (details in frontend README)
```
