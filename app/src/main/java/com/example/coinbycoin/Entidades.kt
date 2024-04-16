package com.example.coinbycoin

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE


@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val usuario: String,
    val contrasena: String,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val documento: String
)

@Entity(foreignKeys = [ForeignKey(entity = Usuario::class, parentColumns = ["id"], childColumns = ["idUsuario"], onDelete = CASCADE)])
data class Ingreso(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val descripcion: String,
    val cantidad: Double,
    val fecha: String,
    val tipo: String,
    val idUsuario: Long
)

@Entity(foreignKeys = [ForeignKey(entity = Usuario::class, parentColumns = ["id"], childColumns = ["idUsuario"], onDelete = CASCADE)])
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val descripcion: String,
    val categoria: String,
    val fecha: String,
    val cantidad: Double,
    val idUsuario: Long
)
