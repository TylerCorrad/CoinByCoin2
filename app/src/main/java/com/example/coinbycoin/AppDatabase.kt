package com.example.coinbycoin
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class, Ingreso::class, Gasto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ingresoDao(): IngresoDao
    abstract fun gastoDao(): GastoDao
}
