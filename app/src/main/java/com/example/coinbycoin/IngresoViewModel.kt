package com.example.coinbycoin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngresoViewModel(application: Application) :AndroidViewModel(application){

    private val allIngresos: LiveData<List<Ingreso>>
    private val repository: IngresoRepository
    init {
        val ingresoDao = AppDatabase.getDatabase(application).ingresoDao()
        repository = IngresoRepository(ingresoDao)
        allIngresos = repository.getAllIngresos()
    }

    fun insertIngreso(ingreso: Ingreso) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIngreso(ingreso)
        }
    }
    fun getIngMesDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>> {
        return repository.getIngMesDeEsteMes(usuarioId)
    }
    fun getIngcasDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>> {
        return repository.getIngCasDeEsteMes(usuarioId)
    }

    fun getIngTotalDeEsteMes(usuarioId: Long): LiveData<Double>{
        return repository.getIngTotalDeEsteMes(usuarioId)
    }

    fun getAllIngresos(): LiveData<List<Ingreso>>{
        return repository.getAllIngresos()
    }
}