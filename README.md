# Android Todo Application

Simple Todo Android application with Firebase backend. Features:

1. Screens: 
    * List of todos
    * Single todo details
    * Creating new todo 
    * Editing existing todo
    * Firebase AuthUI screens
    
2. User authentication by email and password or by federated identity providers (Facebook and Google).

3. Data synchronization across devices.

4. Offline capabilities.

## Important
Google Sign-in will not work unless SHA certificate fingerprints (debug or release) are added to Firebase application settings, as described here:
https://developers.google.com/android/guides/client-auth

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/SasaJug/android-firebase-todo.git
```

## Firebase Configuration
In order to use app with separate Firebase backend it is needed to create the app in Firebase console and replace googleservices.json from the app module.


## Maintainers
This project is maintained by:
* [Sasa Jugurdzija](http://github.com/SasaJug)

## ToDo
1. Implement clean architecture.
2. Decouple Firebase sdk from the rest of the app.


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Run the linter (ruby lint.rb').
5. Push your branch (git push origin my-new-feature)
6. Create a new Pull Request
