package com.marsof.bertra.ui.viewmodels.activetrainstrategies

import com.marsof.bertra.R
import com.marsof.bertra.ui.viewmodels.IActiveTrainStrategy

class NoActiveTrainStrategy() : IActiveTrainStrategy {
    override fun getDisplayTextFromResource(): Int {
        return R.string.no_last_train
    }

    override fun getButtonLabelText(): Int {
        return R.string.choose_train
    }

    override fun executeAction() {
        TODO("Go to the Train List Screen")
    }
}