package com.project.dessertapp.model.state

import com.project.dessertapp.model.entities.Authority

data class UserState(
    var firstName : String,
    var lastName : String,
    var email : String,
    var listOfAuthorities : List<Authority>
)