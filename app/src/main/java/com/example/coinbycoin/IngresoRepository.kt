package com.example.coinbycoin

import androidx.lifecycle.LiveData

class IngresoRepository(private val ingresoDao: IngresoDao) {
    fun getAllIngresos(): LiveData<List<Ingreso>> = ingresoDao.getAll()

    suspend fun insertIngreso(ingreso: Ingreso){
        ingresoDao.insert(ingreso)
    }


    fun getIngMesDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>{
        return ingresoDao.getIngMesDeEsteMes(usuarioId)
    }
    fun getIngCasDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>{
        return ingresoDao.getIngMesDeEsteMes(usuarioId)
    }

    fun getIngTotalDeEsteMes(usuarioId: Long): LiveData<Double>{
        return ingresoDao.getIngTotalDeEsteMes(usuarioId)
    }
}