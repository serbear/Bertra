package com.marsof.bertra.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marsof.bertra.BertraApplication
import com.marsof.bertra.ui.viewmodels.HomeScreenViewModel
import com.marsof.bertra.ui.viewmodels.NewTrainScreenViewModel
import com.marsof.bertra.ui.viewmodels.TrainExercisesScreenViewModel
import com.marsof.bertra.ui.viewmodels.TrainListScreenViewModel

fun CreationExtras.bertraApplication(): BertraApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BertraApplication)

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel()
        }
        initializer {
            TrainListScreenViewModel(bertraApplication().container.trainDao)
        }
        initializer {
            NewTrainScreenViewModel(bertraApplication().container.trainDao)
        }
        initializer {
            TrainExercisesScreenViewModel()
        }
    }
}