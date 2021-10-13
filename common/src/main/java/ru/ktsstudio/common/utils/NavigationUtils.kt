package ru.ktsstudio.common.utils

import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

/**
 * Created by Igor Park on 04/08/2020.
 */
fun NavController.navigateSafe(
    direction: NavDirections,
    navOptions: NavOptions? = null,
    extras: Navigator.Extras? = null
) {
    val currentDestination = currentDestination ?: graph
    val destinationId = direction.actionId
        .let(currentDestination::getAction)
        ?.destinationId
        ?: return

    if (currentDestination.id == destinationId) return

    when {
        extras != null -> navigate(direction, extras)
        navOptions != null -> navigate(direction, navOptions)
        else -> navigate(direction)
    }
}

fun Fragment.navigate(
    direction: NavDirections,
    navOptions: NavOptions? = null,
    extras: Navigator.Extras? = null
) {
    findNavController().navigateSafe(direction, navOptions, extras)
}

fun NavController.resetNavGraph(@NavigationRes navGraph: Int) {
    val newGraph = navInflater.inflate(navGraph)
    graph = newGraph
}
