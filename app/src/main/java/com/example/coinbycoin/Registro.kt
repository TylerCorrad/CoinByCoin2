package com.example.coinbycoin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
class Registro : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnVolver = findViewById<TextView>(R.id.btnVolver)
        val txtInputContrasena2 = findViewById<TextInputEditText>(R.id.txtinputContrasena2)
        val txtInputConf = findViewById<TextInputEditText>(R.id.txtinputConf)
        val txtInputNombres = findViewById<TextInputEditText>(R.id.txtinputNombres)
        val txtInputApellidos = findViewById<TextInputEditText>(R.id.txtinputApellidos)
        val txtInputUsuario = findViewById<TextInputEditText>(R.id.txtinputUsuario)
        val btnRegistro1 = findViewById<Button>(R.id.btnRegistro)
        val txtAdvertencia = findViewById<TextView>(R.id.Advertencia)

        btnRegistro1.setOnClickListener {
            // Verifica si todos los campos están llenos
            if (txtInputNombres.text.isNullOrEmpty() ||
                txtInputApellidos.text.isNullOrEmpty() ||
                txtInputUsuario.text.isNullOrEmpty() ||
                txtInputContrasena2.text.isNullOrEmpty() ||
                txtInputConf.text.isNullOrEmpty()) {
                txtAdvertencia.text = "Todos los campos son obligatorios"
                return@setOnClickListener
            }

            // Verifica si los campos de contraseña coinciden
            if (txtInputContrasena2.text.toString() != txtInputConf.text.toString()) {
                txtAdvertencia.text = "Las contraseñas no coinciden"
                return@setOnClickListener
            }

            // Verifica si la contraseña tiene al menos 8 caracteres, una mayúscula, una minúscula y un carácter especial
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}\$")
            if (!regex.matches(txtInputContrasena2.text.toString())) {
                txtAdvertencia.text = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un carácter especial"
                return@setOnClickListener
            }

            // Aquí se debe poner la funcion para crear el usuario y guardar los datos del usuario
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}