package com.example.coinbycoin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coinbycoin.databinding.FragmentReporteBinding

class Reporte : Fragment() {
    private var usuarioId: Long = -1
    // Declaración de la variable de enlace
    private var _binding: FragmentReporteBinding? = null

    // Esta propiedad es válida solo entre onCreateView y onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento utilizando el enlace de datos generado
        _binding = FragmentReporteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Recuperar el ID del usuario del argumento
        usuarioId = arguments?.getLong("usuario_id", -1) ?: -1

        // Devolver la vista raíz del diseño inflado
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al enlace de datos para evitar fugas de memoria
        _binding = null
    }
}