package com.example.coinbycoin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Registro : AppCompatActivity() {

    private lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        // Inicializar el ViewModel
        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val txtInputContrasena2 = findViewById<TextInputEditText>(R.id.txtinputContrasena2)
        val txtInputConf = findViewById<TextInputEditText>(R.id.txtinputConf)
        val txtInputNombres = findViewById<TextInputEditText>(R.id.txtinputNombres)
        val txtInputApellidos = findViewById<TextInputEditText>(R.id.txtinputApellidos)
        val txtInputUsuario = findViewById<TextInputEditText>(R.id.txtinputUsuario)
        val btnRegistro1 = findViewById<Button>(R.id.btnRegistro)
        val txtAdvertencia = findViewById<TextView>(R.id.Advertencia)

        // Expresión regular para validar la contraseña
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}\$")

        btnRegistro1.setOnClickListener {
            val nombres = txtInputNombres.text.toString()
            val apellidos = txtInputApellidos.text.toString()
            val usuario = txtInputUsuario.text.toString()
            val contrasena = txtInputContrasena2.text.toString()

            // Verificar que todos los campos estén llenos
            if (nombres.isEmpty() || apellidos.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
                txtAdvertencia.text = getString(R.string.todos_los_campos_son_obligatorios)
                return@setOnClickListener
            }

            // Verificar que las contraseñas coincidan
            if (contrasena != txtInputConf.text.toString()) {
                txtAdvertencia.text = getString(R.string.las_contrase_as_no_coinciden)
                return@setOnClickListener
            }

            // Verificar que la contraseña cumpla con los requisitos
            if (!regex.matches(contrasena)) {
                txtAdvertencia.text = getString(R.string.requerimientos_cont)
                return@setOnClickListener
            }

            // Crear el nuevo usuario
            val nuevoUsuario = Usuario(
                nombres = nombres,
                apellidos = apellidos,
                usuario = usuario,
                contrasena = contrasena,
                correo = "",
                telefono = "",
                documento = ""
            )

            insertarUsuario(nuevoUsuario)
        }

        btnVolver.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun insertarUsuario(usuario: Usuario) {
        val txtAdvertencia = findViewById<TextView>(R.id.Advertencia)
        Log.d("RegistroActivity", "iniciando insercion")
        usuarioViewModel.getUsuarioPorUsuario(usuario.usuario).observe(this) { usuarioExistente ->
            if (usuarioExistente != null) {
                // Usuario ya existe en la base de datos
                Log.d("RegistroActivity", "Usuario duplicado")
                txtAdvertencia.text = getString(R.string.error_usuario_duplicado)
            } else {
                Log.d("RegistroActivity", "Insertando nuevo usuario: $usuario")
                // Usuario no existe en la base de datos, insertarlo
                lifecycleScope.launch {
                    usuarioViewModel.insertUsuario(usuario)
                    Log.d("RegistroActivity", "Usuario insertado correctamente")
                }
                // Observa el LiveData del último ID de usuario
                usuarioViewModel.getUltimoUsuarioId().observe(this) { ultimoUsuarioId ->
                    if (ultimoUsuarioId != null) {
                        // Redirigir a la actividad Dashboard con el ID del usuario
                        val intent = Intent(this@Registro, Dashboard::class.java)
                        intent.putExtra("usuario_id", ultimoUsuarioId)
                        startActivity(intent)
                    } else {
                        // Manejar el caso donde no se pudo obtener el ID del usuario
                        txtAdvertencia.text = getString(R.string.error_al_obtener_el_id_del_usuario)
                    }
                }
            }
        }
    }

}
