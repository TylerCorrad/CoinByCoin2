package com.example.coinbycoin

import androidx.lifecycle.LiveData

class GastoRepository(private val gastoDao: GastoDao) {


    fun getAllGastos(): LiveData<List<Gasto>> = gastoDao.getAll()

    fun insertGasto(gasto: Gasto){
        gastoDao.insert(gasto)
    }

    fun getDisponible(usuarioId: Long): LiveData<Double> = gastoDao.getDisponible(usuarioId)

    fun getValorGastosMes(usuarioId: Long): LiveData<Double> = gastoDao.getValorGastosMes(usuarioId)

    fun getValorGastosMesCategoria(usuarioId: Long, categoria:String): LiveData<Double> = gastoDao.getValorGastosMesCategoria(usuarioId, categoria)

    fun getGastosMesCategoria(usuarioId: Long, categoria: String):LiveData<List<Gasto>> = gastoDao.getGastosMesCategoria(usuarioId, categoria)

    fun deleteGasto(id: Long){
        gastoDao.deleteGasto(id)
    }
    fun modificarGasto(id: Long, categoria: String, valor: Double, descripcion: String, fecha: String){
        gastoDao.modificarGasto(id, categoria, valor, descripcion, fecha)
    }

    fun getGastosPorFechas(idUsuario: Long,fechaInf: String,fechaSup: String): LiveData<List<Gasto>>{
        return gastoDao.getGastosPorFechas(idUsuario,fechaInf,fechaSup)
    }
}
