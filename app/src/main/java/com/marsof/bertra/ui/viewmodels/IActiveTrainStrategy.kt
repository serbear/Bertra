package com.marsof.bertra.ui.viewmodels

interface IActiveTrainStrategy {
    fun getDisplayTextFromResource():  Int{
        return TODO("Provide the return value")
    }
    fun getDisplayText(): String{
        return TODO("Provide the return value")
    }
    fun getButtonLabelText(): Int
    fun executeAction()

}