package com.probro.khoded.routing

object Routes {
    const val BASE_USER_ROUTE = "/user"

    sealed class UserRoutes(open val route: String = "") {
        data object LOGIN : UserRoutes() {
            override val route: String = "$BASE_USER_ROUTE/login"
        }

        data object RegisterUser : UserRoutes() {
            override val route: String = "$BASE_USER_ROUTE/register"
        }

        data object UpdateUser : UserRoutes() {
            override val route: String = "$BASE_USER_ROUTE/updateUser"
        }

        data object DeleteUser : UserRoutes() {
            override val route: String = "$BASE_USER_ROUTE/deleteUser"
        }
    }


}