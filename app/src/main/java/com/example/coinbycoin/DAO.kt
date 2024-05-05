package com.example.coinbycoin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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

    @Query("DELETE FROM Usuario WHERE id = :usuarioId")
    fun eliminarUsuario(usuarioId: Long)

    @Query("UPDATE Usuario SET contrasena = :contrasena WHERE ID = :usuarioId")
    fun cambiarContrasena(contrasena: String, usuarioId: Long)
}

@Dao
interface IngresoDao {
    @Query("SELECT * FROM Ingreso")
    fun getAll(): LiveData<List<Ingreso>>

    @Query("SELECT * FROM Ingreso WHERE idUsuario = :usuarioId AND tipo = 'casual' AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now')")
    fun verificacion(usuarioId: Long): LiveData<List<Ingreso>>

    @Query("SELECT * FROM Ingreso WHERE idUsuario = :usuarioId AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now') AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now') AND tipo = 'mensual'")
    fun getIngMesDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>

    @Insert
    fun insert(ingreso: Ingreso)

    @Query("SELECT * FROM Ingreso WHERE idUsuario = :usuarioId AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now') AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now') AND tipo = 'casual'")
    fun getIngCasDeEsteMes(usuarioId: Long): LiveData<List<Ingreso>>

    @Query("SELECT SUM(valor) FROM Ingreso WHERE idUsuario = :usuarioId AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now')AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now')")
    fun getIngTotalDeEsteMes(usuarioId: Long): LiveData<Double>

    @Query("SELECT * FROM Ingreso WHERE idUsuario = :usuarioId AND tipo = 'mensual' AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = :anio AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = :mes")
    fun getIngresosMensuales(usuarioId: Long, anio: String, mes: String): LiveData<List<Ingreso>>


    @Query("DELETE FROM Ingreso")
    fun truncarIngresos()

    @Query("UPDATE Ingreso SET fecha = :fecha, valor = :valor WHERE id = :id ")
    fun modificarIngreso(fecha:String, valor:Double, id:Long)

    @Query("DELETE FROM Ingreso WHERE id = :id")
    fun eliminarIngreso(id: Long)

    @Query("UPDATE Ingreso set tipo = 'mensual Inactivo' WHERE descripcion = :descripcion")
    fun desactivarIngPasado(descripcion: String)

}

@Dao
interface GastoDao {
    @Query("SELECT * FROM Gasto")
    fun getAll(): LiveData<List<Gasto>>

    @Insert
    fun insert(gasto: Gasto)

    @Query("SELECT (SELECT SUM(valor) FROM Ingreso WHERE idUsuario = :usuarioId)-(SELECT SUM(valor) FROM Gasto  WHERE idUsuario = :usuarioId)")
    fun getDisponible(usuarioId: Long): LiveData<Double>

    @Query("SELECT * FROM Gasto WHERE idUsuario = :usuarioId AND categoria = :categoria AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now')AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now')")
    fun getGastosMesCategoria(usuarioId: Long, categoria: String):LiveData<List<Gasto>>

    @Query("SELECT SUM(valor) FROM Gasto WHERE idUsuario = :usuarioId AND categoria = :categoria AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now')AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now')")
    fun getValorGastosMesCategoria(usuarioId: Long, categoria: String):LiveData<Double>

    @Query("SELECT SUM(valor) FROM Gasto WHERE idUsuario = :usuarioId AND SUBSTR(fecha, 1, INSTR(fecha, '-') - 1) = strftime('%Y', 'now')AND SUBSTR(fecha, INSTR(fecha, '-') + 1, 2) = strftime('%m', 'now')")
    fun getValorGastosMes(usuarioId: Long): LiveData<Double>

    @Query("DELETE FROM Gasto WHERE id = :id")
    fun deleteGasto(id: Long)

    @Query("UPDATE gasto SET categoria = :categoria, valor = :valor, descripcion = :descripcion, fecha = :fecha WHERE id == :id")
    fun modificarGasto(id: Long, categoria: String, valor: Double, descripcion: String, fecha: String)
}
