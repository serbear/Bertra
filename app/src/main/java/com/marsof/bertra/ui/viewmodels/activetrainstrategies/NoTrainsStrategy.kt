package com.marsof.bertra.ui.viewmodels.activetrainstrategies

import com.marsof.bertra.R
import com.marsof.bertra.ui.viewmodels.IActiveTrainStrategy

class NoTrainsStrategy() : IActiveTrainStrategy {

    override fun getDisplayTextFromResource(): Int {
        return R.string.no_any_trains
    }

    override fun getButtonLabelText(): Int {
        return R.string.add_train
    }

    override fun executeAction() {
        TODO("Go to the New Train Screen")
    }
}