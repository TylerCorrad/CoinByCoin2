package com.example.coinbycoin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.coinbycoin.databinding.FragmentPerfilBinding
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText

class Perfil : Fragment() {

    // Declaración de la variable de enlace
    private var _binding: FragmentPerfilBinding? = null

    // Esta propiedad es válida solo entre onCreateView y onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento utilizando el enlace de datos generado
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Devolver la vista raíz del diseño inflado
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data class InfoUsuario(var Nombres: String, var Apellidos: String, val Usuario: String, var Documento: String, var NumeroTel:String, var Email:String)
        //aqui se carga la informacion ya guardada del usuario
        val usuario = InfoUsuario("Tyler", "Corradine Vargas", "Tyler47","1000611958", "3053224209", "tylercorrad@gmail.com" )

        val usuarioInputField = view.findViewById<TextInputEditText>(R.id.txtinputUsuario)
        val nombreInputField = view.findViewById<TextInputEditText>(R.id.txtinputNombres)
        val apellidoInputField = view.findViewById<TextInputEditText>(R.id.txtinputApellidos)
        val docInputField = view.findViewById<TextInputEditText>(R.id.txtinputDoc)
        val mailInputField = view.findViewById<TextInputEditText>(R.id.txtinputMail)
        val telInputField = view.findViewById<TextInputEditText>(R.id.txtinputTel)

        usuarioInputField.setText(usuario.Usuario)
        nombreInputField.setText(usuario.Nombres)
        apellidoInputField.setText(usuario.Apellidos)
        docInputField.setText(usuario.Documento)
        mailInputField.setText(usuario.Email)
        telInputField.setText(usuario.NumeroTel)

        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)
        val btnCambiarCon = view.findViewById<Button>(R.id.btnCambiarCon)
        val btnBorrarPerf = view.findViewById<Button>(R.id.btnBorrarPerf)

        btnGuardar.setOnClickListener {
            // Verifica si alguno de los campos está vacío
            val camposVacios = mutableListOf<String>()
            if (usuarioInputField.text.isNullOrBlank()) {
                camposVacios.add("Usuario")
            }
            if (docInputField.text.isNullOrBlank()) {
                camposVacios.add("Documento")
            }
            if (apellidoInputField.text.isNullOrBlank()) {
                camposVacios.add("Apellidos")
            }
            if (nombreInputField.text.isNullOrBlank()) {
                camposVacios.add("Nombres")
            }

            // Si hay campos vacíos, muestra un mensaje
            if (camposVacios.isNotEmpty()) {
                val camposFaltantes = camposVacios.joinToString(", ")
                Toast.makeText(requireContext(), "Se deben llenar los campos: $camposFaltantes", Toast.LENGTH_SHORT).show()
            } else {
                //aqui va el codigo para guardar la informacion
                Toast.makeText(requireContext(), "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnBorrarPerf.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas borrar tu perfil?")
            builder.setPositiveButton("Aceptar") { dialog, _ ->
                // Código para borrar el perfil y volver a la actividad Login
                val intent = Intent(requireContext(), Login::class.java)
                startActivity(intent)
                requireActivity().finish()
                dialog.dismiss() // Cierra el diálogo después de aceptar
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss() // Cierra el diálogo si se hace clic en Cancelar
            }
            val dialog = builder.create()
            dialog.show()
        }

        btnCambiarCon.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al enlace de datos para evitar fugas de memoria
        _binding = null
    }
}