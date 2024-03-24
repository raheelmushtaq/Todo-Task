package com.app.todolist.presentation.screens.splash.state_event


/*
* SplashUIEvent class is used for telling UI/Screen that a action has been completed by viewmodel.
* this class is used for such action, Success is sent when a task is data is fetched.
* error is sent if for some reason  the data is not fetched from api*/
sealed class SplashUIEvent {
    //success event is data is fetched from api
    data object Success : SplashUIEvent()
    //for howing error message if case data is not fethced from api
    data class Error(val message: Int) : SplashUIEvent()
}