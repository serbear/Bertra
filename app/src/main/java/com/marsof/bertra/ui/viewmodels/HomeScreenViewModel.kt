package com.marsof.bertra.ui.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.marsof.bertra.ui.ViewModelProvider


class HomeScreenViewModel() : ViewModel() {
    @Composable
    fun getActiveTrainStrategy(
        viewModel: TrainListScreenViewModel = viewModel(factory = ViewModelProvider.Factory)
    ): IActiveTrainStrategy {
        return viewModel.activeTrainStrategy.collectAsState().value
    }
}