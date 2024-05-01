package com.example.coinbycoin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val idUsuario = MutableLiveData<Long>()

    fun setUsuarioId(data: Long){
        idUsuario.value = data
    }

    fun getUsuarioId(): Any {
        return idUsuario
    }
}