package com.example.sharedcard.ui.startup.data

sealed class TransitionState
class Registration : TransitionState()
class Authorization : TransitionState()
class Continue : TransitionState()
class Back : TransitionState()
