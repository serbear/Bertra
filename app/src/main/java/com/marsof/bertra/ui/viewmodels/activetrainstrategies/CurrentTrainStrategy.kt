package com.marsof.bertra.ui.viewmodels.activetrainstrategies

import com.marsof.bertra.R
import com.marsof.bertra.ui.viewmodels.IActiveTrainStrategy

class CurrentTrainStrategy() : IActiveTrainStrategy {
    override fun getDisplayText(): String {
        return "::train data::"
    }

    override fun getButtonLabelText(): Int {
        return R.string.continue_train
    }

    override fun executeAction() {
        TODO("Go to the Training Screen")
    }
}