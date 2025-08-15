package com.marsof.bertra.ui.navigation

interface INavigationDestination {
    /**
     * Unique name to define the path for a composable.
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int
}