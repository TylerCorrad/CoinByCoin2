package com.example.coinbycoin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.coinbycoin.databinding.FragmentDashboardBinding
import com.example.coinbycoin.ui.theme.CustomSpinnerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardFragment : Fragment() {

    // Declaración de la variable de enlace
    private var _binding: FragmentDashboardBinding? = null

    // Esta propiedad es válida solo entre onCreateView y onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el diseño del fragmento utilizando el enlace de datos generado
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Devolver la vista raíz del diseño inflado
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnNuevoGasto = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        btnNuevoGasto.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_gasto, null)
            val spinnerCategoria = dialogView.findViewById<Spinner>(R.id.spinnerCategoria)
            val items = resources.getStringArray(R.array.categorias).toList()
            val adapter = CustomSpinnerAdapter(requireContext(), items)
            spinnerCategoria.adapter = adapter
            val editTextCantidad = dialogView.findViewById<EditText>(R.id.editTextCantidad)
            val editTextFecha = dialogView.findViewById<EditText>(R.id.editTextFecha)
            val editTextDescripcion = dialogView.findViewById<EditText>(R.id.editTextDescripcion)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Guardar") { dialog, _ ->
                    // Aquí puedes obtener los valores ingresados por el usuario y realizar las acciones necesarias
                    val categoria = spinnerCategoria.selectedItem.toString()
                    val cantidad = editTextCantidad.text.toString()
                    val fecha = editTextFecha.text.toString()
                    val descripcion = editTextDescripcion.text.toString()

                    // Implementar aquí la lógica para guardar los datos del nuevo gasto
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al enlace de datos para evitar fugas de memoria
        _binding = null
    }
}