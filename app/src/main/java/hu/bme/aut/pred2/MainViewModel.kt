package hu.bme.aut.pred2

import androidx.lifecycle.ViewModel
import hu.bme.aut.pred2.data.TeamRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    teamRepository: TeamRepository
): ViewModel(){


    val readAll = teamRepository.readAll
}