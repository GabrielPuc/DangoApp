package com.gdbm.dangoapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.components.items.SettingsButton
import com.gdbm.dangoapp.ui.screens.Main
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.utils.NavigationItem
import com.gdbm.dangoapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationContainer(mainViewModel:MainViewModel, title:String) {

    val navController = rememberNavController()

    val items = listOf(
        NavigationItem.Reference,
        NavigationItem.Practice
    )


    Scaffold(
        topBar = { NavBar(title = title, actions = {
            SettingsButton {
                mainViewModel.select("SETTINGS")
            }
        }) },
        bottomBar = {
            BottomNavigation(backgroundColor = CustomColorsPalette.current.primary) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route


                items.forEach {
                    BottomNavigationItem(selected = currentRoute == it.route,
                        label = {
                            Text(
                                text = it.label,
                                color = if (currentRoute == it.route) CustomColorsPalette.current.primaryContainerColor else CustomColorsPalette.current.textColor
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = it.icons, contentDescription = null,
                                tint = if (currentRoute == it.route) CustomColorsPalette.current.primaryContainerColor else CustomColorsPalette.current.textColor
                            )

                        },

                        onClick = {
                            if(currentRoute!=it.route){

                                navController.graph.startDestinationRoute?.let {
                                    navController.popBackStack(it,true)
                                }

                                navController.navigate(it.route){
                                    launchSingleTop = true
                                }

                            }

                        })

                }


            }


        }) { padding ->

        Box(modifier = Modifier.padding(padding)){
            NavigationController(navController = navController, viewModel= mainViewModel)
        }


    }

}

@Composable
private fun NavigationController(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = NavigationItem.Reference.route) {

        composable(NavigationItem.Reference.route) {
            Main(mainViewModel = viewModel, content = Configs.OPTIONS_REFERENCE_SCREEN)
        }

        composable(NavigationItem.Practice.route) {
            Main(mainViewModel = viewModel, content = Configs.OPTIONS_PRACTICE_SCREEN)
        }
    }


}