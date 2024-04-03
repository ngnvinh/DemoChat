package com.example.tuj_chat.data

data class User(val id: String, val email: String, val avatar: String){
    constructor():this("","","")
}