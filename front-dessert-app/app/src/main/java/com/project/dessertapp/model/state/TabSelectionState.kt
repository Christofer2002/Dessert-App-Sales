package com.project.dessertapp.model.state

data class TabSelectionState(
    val isWaitingSelected: Boolean = false,
    val isAcceptedSelected: Boolean = false,
    val isRejectedSelected: Boolean = false
)