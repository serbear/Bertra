package com.marsof.bertra.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.marsof.bertra.BertraApplication
import com.marsof.bertra.data.repository.MusclesRepository
import com.marsof.bertra.ui.viewmodels.ActiveWorkoutScreenViewModel
import com.marsof.bertra.ui.viewmodels.AddTrainExerciseScreenViewModel
import com.marsof.bertra.ui.viewmodels.ExerciseListScreenViewModel
import com.marsof.bertra.ui.viewmodels.ExercisesApiScreenViewModel
import com.marsof.bertra.ui.viewmodels.HomeScreenViewModel
import com.marsof.bertra.ui.viewmodels.MeasurementUnitListScreenViewModel
import com.marsof.bertra.ui.viewmodels.NewExerciseScreenViewModel
import com.marsof.bertra.ui.viewmodels.NewMeasurementUnitScreenViewModel
import com.marsof.bertra.ui.viewmodels.NewTrainScreenViewModel
import com.marsof.bertra.ui.viewmodels.SettingsScreenViewModel
import com.marsof.bertra.ui.viewmodels.TrainExercisesListScreenViewModel
import com.marsof.bertra.ui.viewmodels.TrainListScreenViewModel
import com.marsof.bertra.ui.viewmodels.WorkoutEngageScreenViewModel

fun CreationExtras.bertraApplication(): BertraApplication = (
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BertraApplication
        )

object ViewModelProvider {
    val AppViewModelProvider = viewModelFactory {
        initializer {
            HomeScreenViewModel()
        }
        initializer {
            TrainListScreenViewModel(
                bertraApplication().container.trainDao,
            )
        }
        initializer {
            NewTrainScreenViewModel(
                bertraApplication().container.trainDao,
            )
        }
        initializer {
            TrainExercisesListScreenViewModel(
                bertraApplication().container.trainExerciseDao,
            )
        }
        initializer {
            NewExerciseScreenViewModel(
                bertraApplication().container.exerciseDao,
            )
        }
        initializer {
            ExerciseListScreenViewModel(
                bertraApplication().container.exerciseDao,
            )
        }
        initializer {
            MeasurementUnitListScreenViewModel(
                bertraApplication().container.measurementUnitDao,
            )
        }
        initializer {
            NewMeasurementUnitScreenViewModel(
                bertraApplication().container.measurementUnitDao,
            )
        }
        initializer {
            AddTrainExerciseScreenViewModel(
                bertraApplication().container.trainExerciseDao,
                bertraApplication().container.exerciseDao,
                bertraApplication().container.measurementUnitDao,
                bertraApplication().container.trainExerciseRepetitionsDao,
            )
        }
        initializer {
            WorkoutEngageScreenViewModel(
                bertraApplication().container.trainExerciseDao,
                bertraApplication().container.trainDao,
            )
        }
        initializer {
            ActiveWorkoutScreenViewModel(
                bertraApplication().container.trainDao,
                bertraApplication().container.trainExerciseDao,
                bertraApplication().container.trainExerciseRepetitionsDao,
            )
        }
        initializer {
            SettingsScreenViewModel(
                bertraApplication()
            )
        }
        initializer {
            // Создаем репозиторий с вашим ключом
            val musclesRepository =
                MusclesRepository(apiKey = "51FLfkCz1w+K0y99y9XYzA==hKlVez5hTPgaa5TA")
            ExercisesApiScreenViewModel(musclesRepository = musclesRepository)
        }
    }
}
