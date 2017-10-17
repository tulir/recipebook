# RecipeBook frontend

This is the frontend. It only uses React and VanillaJS.

## Structure
```
Frontend
├───public                // HTML, app manifest, favicon
├───src                   // JSX, Sass
│   │   index.js            // React index file
│   │   recipebook.jsx      // App index file
│   ├───res                 // Images used within app
│   ├───style               // Sass stylesheets
│   └───components          // React components used in app
│       │   index.sass        // Index file that includes all other files
│       ├───base              // Base stylesheets (variables, global styling)
│       └───components        // Component-specific stylesheets
│
├───scripts               // React build scripts
├───config                // React build configs
├───node_modules          // Node module directory (ignored in Git)
└───webapp                // Build directory (ignored in Git)
```
