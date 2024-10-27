package com.dawinder.btnjc.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(),
            title = NavTitle.HOME,
            icon = Icons.Default.Home)

    object Search :
        Item(
            path = NavPath.SEARCH.toString(),
            title = NavTitle.SEARCH,
            icon = Icons.Default.Search,
        )

    object List :
        Item(path = NavPath.LIST.toString(),
            title = NavTitle.LIST,
            icon = Icons.Default.List)

    object Profile :
        Item(
            path = NavPath.PROFILE.toString(),
            title = NavTitle.PROFILE,
            icon = Icons.Default.Person,
        )

    object Other :
        Item(
            path = NavPath.OTHER.toString(),
            title = NavTitle.OTHER,
            icon = Icons.Default.Settings,
        )

    object Users :
        Item(
            path = NavPath.USERS.toString(),
            title = NavTitle.OTHER,
            icon = Icons.Default.Person,
        )


}