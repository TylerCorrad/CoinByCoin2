package com.example.coinbycoin
import androidx.lifecycle.LiveData
import com.example.coinbycoin.AppDatabase
import com.example.coinbycoin.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    fun getAllUsuarios(): LiveData<List<Usuario>> = usuarioDao.getAll()


    suspend fun insertUsuario(usuario: Usuario){
        usuarioDao.insert(usuario)
    }

    fun getUsuarioPorId(id: Long): LiveData<Usuario>{
        return usuarioDao.getUsuarioPorId(id)
    }

    fun getUsuarioPorUsuario(nombreUsuario: String): LiveData<Usuario?>{
        return usuarioDao.getUsuarioPorUsuario(nombreUsuario)
    }

    fun getUltimoUsuarioId():LiveData<Long>{
        return usuarioDao.getUltimoUsuarioId()
    }

    fun actualizarUsuario(usuarioId: Long, usuario: String, nombres: String, apellidos: String, documento: String, email: String, numeroTel: String) {
        usuarioDao.actualizarUsuario(usuarioId= usuarioId, usuario=usuario, nombres=nombres, apellidos=apellidos, documento=documento, email=email, numeroTel=numeroTel)
    }
}
