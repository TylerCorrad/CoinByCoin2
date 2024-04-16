package com.example.coinbycoin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM Usuario")
    fun getAll(): List<Usuario>

    @Insert
    fun insert(usuario: Usuario)
}

@Dao
interface IngresoDao {
    @Query("SELECT * FROM Ingreso")
    fun getAll(): List<Ingreso>

    @Insert
    fun insert(ingreso: Ingreso)
}

@Dao
interface GastoDao {
    @Query("SELECT * FROM Gasto")
    fun getAll(): List<Gasto>

    @Insert
    fun insert(gasto: Gasto)
}
