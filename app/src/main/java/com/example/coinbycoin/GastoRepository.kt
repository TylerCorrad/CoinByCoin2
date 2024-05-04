package com.example.coinbycoin

import androidx.lifecycle.LiveData

class GastoRepository(private val gastoDao: GastoDao) {

    fun getAllGastos(): LiveData<List<Gasto>> = gastoDao.getAll()

    fun insertGasto(gasto: Gasto){
        gastoDao.insert(gasto)
    }

    fun getDisponible(usuarioId: Long): LiveData<Double> = gastoDao.getDisponible(usuarioId)

    fun getValorGastosMes(usuarioId: Long): LiveData<Double> = gastoDao.getValorGastosMes(usuarioId)

    fun getValorGastosMesCategoria(usuarioId: Long, categoria:String): LiveData<Double> = gastoDao.getGastosMesCategoria(usuarioId, categoria)

}