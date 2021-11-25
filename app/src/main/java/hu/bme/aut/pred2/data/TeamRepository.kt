package hu.bme.aut.pred2.data

import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val teamDao: TeamDao
){
    val readAll = teamDao.readAll()
}