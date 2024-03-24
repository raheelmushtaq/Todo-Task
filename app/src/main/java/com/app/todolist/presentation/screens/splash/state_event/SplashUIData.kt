package com.app.todolist.presentation.screens.splash.state_event


/*
* SplashUIData is used for saving data of the SplashScreen amd SplashViewMode.
* when api io fetching then show loader, and if api fails is
* */
data class SplashUIData(val isLoading: Boolean = false, val isError: Boolean = false)
