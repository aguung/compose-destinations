# Compose Destinations

### What?

Compose destinations is a library to use alongside compose navigation. If you are already using compose navigation, then this will be a good and easy addition that
will make adding new destinations have less boilerplate and be less error-prone, as well as enabling type-safe navigation.

### Why?

Because:
- How nice would it be if navigation compose did not rely as much on bundles, strings and other not type-safe stuff?
- What if we could simply add parameters to the `Composable` function to define the destination arguments?
- And what if we did not have to do anything else other than create the screen composable when we add a new screen? (Instead of remembering 
to add a `composable` call in the `NavHost` and most likely adding the destination to some kind of `sealed class`)

### How?

1. Start by annotating the `Composable` functions that you want to add to the navigation graph with `@Destination`.

```kotlin
@Destination(route = "home", start = true)
@Composable
fun HomeScreen(
    navController: NavController,
    scaffoldState: ScaffoldState
) {
	//...
}
```

2. Replace your `NavHost` call with `Destinations.NavHost` (or if using a `Scaffold`, then replace it with `Destinations.Scaffold`). 
You can also remove the builder blocks.

```kotlin
Destinations.NavHost(
    navController = rememberNavController()
)
```
OR
```kotlin
Destinations.Scaffold(
    scaffoldState = rememberScaffoldState(),
    navController = rememberNavController(),
)
```

3. If the destination has arguments, then simply add them to the `Composable` function!

```kotlin
@Destination(route = "user")
@Composable
fun UserScreen(
    userId: Long
)
```

Then, to navigate to the User Screen, anywhere you have the `navController`.

```kotlin
navController.navigate(UserScreenDestination.withArgs(userId = 1))
```

That's it! No messing with `NavType`, weird routes, bundles and strings. All this will be taken care for you.

Oh and by the way, what if the destination has default arguments? Wouldn't it be nice if we could just use Kotlin default parameters feature?
Well, that is exactly how we do it:

```kotlin
@Destination(route = "user")
@Composable
fun UserScreen(
    userId: Long,
    isOwnUser: Boolean = false
)
```

Now the IDE will even tell you the default arguments of the composable when calling the `withArgs` method!

NOTE: Currently, arguments do have some limitations:
- They must be one of `String`, `Boolean`, `Int`, `Float`, `Long`, `NavController`, `NavBackStackEntry` or `ScaffoldState` (only if you are using `Scaffold`).
- All `Composable` function arguments will be considered navigation arguments except for `NavController`, `NavBackStackEntry` or `ScaffoldState`.
If you have a view model that you want to access with, for example `hiltViewModel()`, it cannot be a Destination argument. The good practice is to call other
`Composable` function and use the Destination Composable as a wrapper to the testable `Composable` screen.
- Default values must be resolvable from the generated `DestinationSpec` class. The code written after the "`=`" will be exactly what is used inside the generated classes. Unfortunately, this means you won't be able to use a private constant as the default value. We'll be looking for ways to improve this.

### Going deeper:

- All annotated composables will generate an implementation of `DestinationSpec` which is a sealed interface that contains the full route, navigation arguments,
`Content` composable function that will call your `Composable` and the `withArgs` implementation.
- `Scaffold` composable lambda parameters will be given a current `DestinationSpec`. This makes it trivial to have top bar, bottom bar and drawer depend on the current destination.
- If you would like to have additional properties/functions in the `DestinationSpec` (for example a "title" which will be shown to the user for each screen) you can make an extension 
property/function of `DestinationSpec`. Since it is a sealed interface, a sealed `when` expression will make sure you always have a definition for each screen (check 
`DestinationSpecExtensions.kt` file in the sample app).

### Current state

At the moment I haven't done any release because I don't really want people to be using this just yet on real projects. I want to be able to change the APIs without worries.
I'm looking for all kinds of feedback, issues, feature requests and help in improving the code or even this README. So please, if you find this interesting, try it out in some sample projects and let me know how it goes! 


### Import it

For now, in order to try Compose Destinations, add jitpack repository and point to a specific commit. For the public releases, I may change it to maven central.

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
	repositories {
		//...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the ksp plugin like this:
```gradle
plugins {
	//...
	id("com.google.devtools.ksp") version "1.5.21-1.0.0-beta07" // This will change to the stable ksp version when compose allows us to use kotlin 1.5.30
}
```

Add the dependencies:
```gradle
    implementation 'com.github.raamcosta.compose-destinations:core:e5ff2ae7db'
    ksp 'com.github.raamcosta.compose-destinations:ksp:e5ff2ae7db'

```

## License

    Copyright 2021 Rafael Costa

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.