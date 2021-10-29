package com.ramcosta.composedestinations.codegen.templates

import com.ramcosta.composedestinations.codegen.commons.*

//region anchors
const val DEFAULT_NAV_CONTROLLER_PLACEHOLDER = "[DEFAULT_NAV_CONTROLLER_PLACEHOLDER]"
const val EXPERIMENTAL_API_PLACEHOLDER = "[EXPERIMENTAL_API_PLACEHOLDER]"
const val ANIMATION_DEFAULT_PARAMS_PLACEHOLDER = "[ANIMATION_DEFAULT_PARAMS_PLACEHOLDER]"
const val ANIMATED_NAV_HOST_CALL_PARAMETERS_START = "[ANIMATED_NAV_HOST_CALL_PARAMETERS_START]"
const val ANIMATED_NAV_HOST_CALL_PARAMETERS_END = "[ANIMATED_NAV_HOST_CALL_PARAMETERS_END]"
const val INNER_NAV_HOST_CALL_ANIMATED_PARAMETERS_START = "[INNER_NAV_HOST_CALL_ANIMATED_PARAMETERS_START]"
const val INNER_NAV_HOST_CALL_ANIMATED_PARAMETERS_END = "[INNER_NAV_HOST_CALL_ANIMATED_PARAMETERS_END]"
const val NAV_HOST_METHOD_NAME = "[NAV_HOST_METHOD_NAME]"
const val NAV_HOST_METHOD_QUALIFIED_NAME = "[NAV_HOST_METHOD_QUALIFIED_NAME]"
const val ADD_ANIMATED_COMPOSABLE_START = "[ADD_ANIMATED_COMPOSABLE_START]"
const val ADD_ANIMATED_COMPOSABLE_END = "[ADD_ANIMATED_COMPOSABLE_END]"
const val ADD_BOTTOM_SHEET_COMPOSABLE_START = "[ADD_BOTTOM_SHEET_COMPOSABLE_START]"
const val ADD_BOTTOM_SHEET_COMPOSABLE_END = "[ADD_BOTTOM_SHEET_COMPOSABLE_END]"
const val ADD_COMPOSABLE_WHEN_ELSE_START = "[ADD_COMPOSABLES_WHEN_ELSE_START]"
const val ADD_COMPOSABLE_WHEN_ELSE_END = "[ADD_COMPOSABLES_WHEN_ELSE_END]"
const val ANIMATED_VISIBILITY_TO_CONTENT_START = "[ANIMATED_VISIBILITY_TO_CONTENT_START]"
const val ANIMATED_VISIBILITY_TO_CONTENT_END = "[ANIMATED_VISIBILITY_TO_CONTENT_END]"

const val START_ACCOMPANIST_NAVIGATION_IMPORTS = "//region accompanist navigation imports"
const val END_ACCOMPANIST_NAVIGATION_IMPORTS = "//endregion accompanist navigation imports"

const val START_ACCOMPANIST_MATERIAL_IMPORTS = "//region accompanist material imports"
const val END_ACCOMPANIST_MATERIAL_IMPORTS = "//endregion accompanist material imports"

const val START_ACCOMPANIST_NAVIGATION = "//region accompanist navigation"
const val END_ACCOMPANIST_NAVIGATION = "//endregion accompanist navigation"

const val START_ACCOMPANIST_MATERIAL = "//region accompanist material"
const val END_ACCOMPANIST_MATERIAL = "//endregion accompanist material"

//endregion

val destinationsNavHostTemplate = """
package $PACKAGE_NAME

import androidx.compose.animation.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.Navigator
import $PACKAGE_NAME.spec.DestinationSpec
import $PACKAGE_NAME.spec.DestinationStyle
import $PACKAGE_NAME.spec.NavGraphSpec
import $PACKAGE_NAME.navigation.DependenciesContainerBuilder
import $PACKAGE_NAME.navigation.dependency
$START_ACCOMPANIST_NAVIGATION_IMPORTS
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.navigation.animation.composable
$END_ACCOMPANIST_NAVIGATION_IMPORTS
$START_ACCOMPANIST_MATERIAL_IMPORTS
import androidx.compose.foundation.layout.ColumnScope
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
$END_ACCOMPANIST_MATERIAL_IMPORTS

//region NavHost
/**
 * Like [$NAV_HOST_METHOD_QUALIFIED_NAME] but includes the destinations of [navGraph].
 * Composables annotated with `@$DESTINATION_ANNOTATION` will belong to a [$GENERATED_NAV_GRAPH] inside [$GENERATED_NAV_GRAPHS_OBJECT].
 *
 * @see [$NAV_HOST_METHOD_QUALIFIED_NAME]
 *
 * @param navGraph [$GENERATED_NAV_GRAPHS_OBJECT] with the destinations to add to NavHost. By default,
 * it will be the 'root' one which is the one with all annotated destinations
 * that don't specify other 'navGraph'
 * @param startDestination the start destination to use
 * @param navController [NavHostController]
 * @param modifier [Modifier]
 * @param dependenciesContainerBuilder lambda invoked when a destination gets navigated to. It allows
 * the caller to contribute certain dependencies that the destination can use.
 */
$EXPERIMENTAL_API_PLACEHOLDER@Composable
fun $DESTINATIONS_NAV_HOST(
    navGraph: $GENERATED_NAV_GRAPH = ${GENERATED_NAV_GRAPHS_OBJECT}.root,
    startDestination: $GENERATED_DESTINATION = navGraph.startDestination,$ANIMATION_DEFAULT_PARAMS_PLACEHOLDER
    navController: NavHostController = rememberDestinationsNavController(),
    modifier: Modifier = Modifier,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit = {}
) {
    $NAV_HOST_METHOD_NAME(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier,
        route = navGraph.route,$ANIMATED_NAV_HOST_CALL_PARAMETERS_START
        contentAlignment = defaultAnimationParams.contentAlignment,
        enterTransition = defaultAnimationParams.enterTransition?.run { { i, t -> enter(i.navDestination, t.navDestination) } },
        exitTransition = defaultAnimationParams.exitTransition?.run{ {i, t -> exit(i.navDestination, t.navDestination) } },
        popEnterTransition = defaultAnimationParams.popEnterTransition?.run{ {i, t -> enter(i.navDestination, t.navDestination) } },
        popExitTransition = defaultAnimationParams.popExitTransition?.run{ {i, t -> exit(i.navDestination, t.navDestination) } },$ANIMATED_NAV_HOST_CALL_PARAMETERS_END
    ) {
        addNavGraphDestinations(
            navGraphSpec = navGraph,
            addNavigation = addNavigation(),   
            addComposable = addComposable(navController, dependenciesContainerBuilder)
        )
    }
}
//endregion NavHost

//region NavController
/**
 * Wraps the correct `remember*NavController` method depending on
 * whether animations are available or not.
 */
$EXPERIMENTAL_API_PLACEHOLDER@Composable
fun rememberDestinationsNavController(
    vararg navigators: Navigator<out NavDestination>
) = $DEFAULT_NAV_CONTROLLER_PLACEHOLDER(*navigators)
//endregion

//region internals
${EXPERIMENTAL_API_PLACEHOLDER}private fun addComposable(
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit
): NavGraphBuilder.($CORE_DESTINATION_SPEC) -> Unit {
    return { destination ->
        destination as $GENERATED_DESTINATION
        val destinationStyle = destination.style
        when (destinationStyle) {
            is DestinationStyle.Default -> {
                addComposable(
                    destination,
                    navController,
                    dependenciesContainerBuilder
                )
            }

            is DestinationStyle.Dialog -> {
                addDialogComposable(
                    destinationStyle,
                    destination,
                    navController,
                    dependenciesContainerBuilder
                )
            }
$ADD_ANIMATED_COMPOSABLE_START
            is DestinationStyle.Animated<*> -> {
                addAnimatedComposable(
                    destinationStyle as AnimatedDestinationStyle,
                    destination,
                    navController,
                    dependenciesContainerBuilder
                )
            }
$ADD_ANIMATED_COMPOSABLE_END$ADD_BOTTOM_SHEET_COMPOSABLE_START
            is DestinationStyle.BottomSheet -> {
                addBottomSheetComposable(
                    destination,
                    navController,
                    dependenciesContainerBuilder
                )
            }
$ADD_BOTTOM_SHEET_COMPOSABLE_END$ADD_COMPOSABLE_WHEN_ELSE_START
            else -> throw RuntimeException("Should be impossible! Code gen should have failed if using a style for which you don't have the dependency")
$ADD_COMPOSABLE_WHEN_ELSE_END        }
    }
}

${EXPERIMENTAL_API_PLACEHOLDER}private fun NavGraphBuilder.addComposable(
    destination: $GENERATED_DESTINATION,
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit
) {
    composable(
        route = destination.route,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks
    ) { navBackStackEntry ->
        destination.Content(
            navController,
            navBackStackEntry,
            {$ANIMATED_VISIBILITY_TO_CONTENT_START 
                dependency<$ANIMATED_VISIBILITY_SCOPE_SIMPLE_NAME>(this@composable)$ANIMATED_VISIBILITY_TO_CONTENT_END
                dependenciesContainerBuilder(destination)
            }
        )
    }
}

private fun NavGraphBuilder.addDialogComposable(
    dialogStyle: DestinationStyle.Dialog,
    destination: $GENERATED_DESTINATION,
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit
) {
    dialog(
        destination.route,
        destination.arguments,
        destination.deepLinks,
        dialogStyle.properties
    ) {
        destination.Content(
            navController = navController,
            navBackStackEntry = it,
            dependenciesContainerBuilder = { dependenciesContainerBuilder(destination) }
        )
    }
}

${EXPERIMENTAL_API_PLACEHOLDER}private fun addNavigation(): NavGraphBuilder.($CORE_NAV_GRAPH_SPEC, NavGraphBuilder.() -> Unit) -> Unit {
    return { navGraph, builder ->
        navigation(
            navGraph.startDestination.route,
            navGraph.route
        ) {
            this.builder()
        }
    }
}

$START_ACCOMPANIST_NAVIGATION
@ExperimentalAnimationApi
private fun NavGraphBuilder.addAnimatedComposable(
    animatedStyle: AnimatedDestinationStyle,
    destination: Destination,
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit
) = with(animatedStyle) {
    composable(
        route = destination.route,
        arguments = destination.arguments,
        deepLinks = destination.deepLinks,
        enterTransition = { i, t -> enterTransition(i.navDestination, t.navDestination) },
        exitTransition = { i, t -> exitTransition(i.navDestination, t.navDestination) },
        popEnterTransition = { i, t -> popEnterTransition(i.navDestination, t.navDestination) },
        popExitTransition = { i, t -> popExitTransition(i.navDestination, t.navDestination) }
    ) { navBackStackEntry ->
        destination.Content(
            navController,
            navBackStackEntry,
            {
                dependency<$ANIMATED_VISIBILITY_SCOPE_SIMPLE_NAME>(this@composable)
                dependenciesContainerBuilder(destination)
            }
        )
    }
}
$END_ACCOMPANIST_NAVIGATION

$START_ACCOMPANIST_MATERIAL
@ExperimentalMaterialNavigationApi
private fun NavGraphBuilder.addBottomSheetComposable(
    destination: $GENERATED_DESTINATION,
    navController: NavHostController,
    dependenciesContainerBuilder: @Composable DependenciesContainerBuilder.($GENERATED_DESTINATION) -> Unit
) {
    bottomSheet(
        destination.route,
        destination.arguments,
        destination.deepLinks
    ) { navBackStackEntry ->
        destination.Content(
            navController,
            navBackStackEntry,
            {
                dependency<$COLUMN_SCOPE_SIMPLE_NAME>(this@bottomSheet)
                dependenciesContainerBuilder(destination)
            }
        )
    }
}
$END_ACCOMPANIST_MATERIAL
//endregion
""".trimIndent()