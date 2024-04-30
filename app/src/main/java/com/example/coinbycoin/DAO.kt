package com.example.coinbycoin

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM Usuario")
    fun getAll(): LiveData<List<Usuario>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE id = :usuarioId")
    fun getUsuarioPorId(usuarioId: Long): LiveData<Usuario>

    @Query("SELECT * FROM usuario WHERE usuario = :nombreUsuario")
    fun getUsuarioPorUsuario(nombreUsuario: String): LiveData<Usuario?>

    @Query("SELECT id FROM Usuario ORDER BY id DESC LIMIT 1")
    fun getUltimoUsuarioId(): LiveData<Long>

    @Query("UPDATE Usuario SET usuario = :usuario, nombres = :nombres, apellidos = :apellidos, documento = :documento, correo = :email, telefono = :numeroTel WHERE id = :usuarioId")
    fun actualizarUsuario(usuarioId: Long, usuario: String, nombres: String, apellidos: String, documento: String, email: String, numeroTel: String)
}

@Dao
interface IngresoDao {
    @Query("SELECT * FROM Ingreso")
    fun getAll(): LiveData<List<Ingreso>>

    @Insert
    fun insert(ingreso: Ingreso)

    @Query("SELECT * FROM Ingreso WHERE tipo = 'mensual' AND idUsuario = :usuarioId AND strftime('%Y', fecha, 'unixepoch') = strftime('%Y', 'now')AND strftime('%m', fecha, 'unixepoch') = strftime('%m', 'now')" )
    fun getIngMesDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>

    @Query("SELECT * FROM Ingreso WHERE tipo = 'casual' AND idUsuario = :usuarioId AND strftime('%Y', fecha, 'unixepoch') = strftime('%Y', 'now') AND strftime('%m', fecha, 'unixepoch') = strftime('%m', 'now')")
    fun getIngCasDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>


}

@Dao
interface GastoDao {
    @Query("SELECT * FROM Gasto")
    fun getAll(): LiveData<List<Gasto>>

    @Insert
    fun insert(gasto: Gasto)
}
