Android IceWarp Task
This project is an Android application developed as part of the IceWarp Android assignment.
The app is built using modern Android development practices, with Jetpack Compose as the primary UI framework.

✨ Features
User login with IceWarp credentials
Persistent login until explicit logout
Channels list fetched using authenticated token
Loading and error state handling
Local data persistence using Sharepreference

Tech Stack

Language: Kotlin
UI: Jetpack Compose
Networking: Retrofit
Asynchronous:RxJava (Login API)
Kotlin Coroutines (Channels API)
Database: Sharepreference

App Flow
🔐 Login Screen

Built using Jetpack Compose
Uses RxJava for login API call
Calls iwauthentication.login.plain
Validates user inputs (email, password)
Saves authentication token in local database

📋 Channels Screen

Built using Jetpack Compose
Uses Kotlin Coroutines for API call
Calls channels.list API using stored token

