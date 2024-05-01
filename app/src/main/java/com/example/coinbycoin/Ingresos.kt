package com.example.coinbycoin

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.coinbycoin.databinding.FragmentIngresosBinding
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.coinbycoin.ui.theme.CustomSpinnerAdapter
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import kotlin.properties.Delegates

class Ingresos : Fragment() {

    private var usuarioId: Long = -1
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var ingresoViewModel: IngresoViewModel
    // Creación de las listas mutables
    private var ingresosMensuales: MutableList<Ingreso> = mutableListOf()
    private var ingresosCasuales: MutableList<Ingreso> = mutableListOf()
    private var totalIngresos :Double = 0.00

    // Declaración de la variable de enlace
    private var _binding: FragmentIngresosBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngresosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingresoViewModel = ViewModelProvider(this).get(IngresoViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.idUsuario.observe(viewLifecycleOwner, Observer { usuarioId ->
            Log.d("FragmentIngresos", "id usuario: $usuarioId")
            if (usuarioId != null) {

                ingresoViewModel.getAllIngresos().observe(viewLifecycleOwner){ingresos ->ingresos.let {
                    Log.d("FragmentIngresos", "ingresos: $it")
                } }


                ingresoViewModel.getIngMesDeEsteMes(usuarioId).observe(viewLifecycleOwner) { ingMensuales ->
                    ingMensuales?.let {
                        ingresosMensuales = it.toMutableList()
                    }
                }
                Log.d("FragmentIngresos", "ingresos mensuales: $ingresosMensuales")
                ingresoViewModel.getIngcasDeEsteMes(usuarioId).observe(viewLifecycleOwner) { ingCasuales ->
                    ingCasuales?.let {
                        ingresosCasuales = it.toMutableList()
                    }
                }
                Log.d("FragmentIngresos", "ingresos casuales: $ingresosCasuales")
                ingresoViewModel.getIngTotalDeEsteMes(usuarioId).observe(viewLifecycleOwner){ ingTotal -> ingTotal?.let {
                    totalIngresos = it
                }}
                Log.d("FragmentIngresos", "ingresos totales: $totalIngresos")
                    val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val totalIngresosFormateado = "${numberFormat.format(totalIngresos)}$"
                val txtBlanco = view.findViewById<TextView>(R.id.txtBlanco)
                txtBlanco.text = totalIngresosFormateado

                val btnNuevoIngresoMes = view.findViewById<ConstraintLayout>(R.id.btnNuevoIngresoMes)
                val btnNuevoIngresoCas = view.findViewById<ConstraintLayout>(R.id.btnNuevoIngresoCas)
                val contenedor_ingresos_cas = view.findViewById<LinearLayout>(R.id.contenedor_ingresos_cas)
                val contenedor_ingresos_mes = view.findViewById<LinearLayout>(R.id.contenedor_ingresos_mes)

                txtBlanco.text = totalIngresosFormateado

                btnNuevoIngresoCas.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_ingreso_cas, null)
                    val editTextCantidad = dialogView.findViewById<TextInputEditText>(R.id.editTextCantidad)
                    val editTextFecha = dialogView.findViewById<EditText>(R.id.editTextFecha)
                    val editTextDescripcion = dialogView.findViewById<EditText>(R.id.editTextDescripcion)

                    editTextFecha.setOnClickListener {
                        showDatePickerDialog(editTextFecha)
                    }

                    val dialog = AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Guardar") { dialog, _ ->
                            val cantidad = editTextCantidad.text.toString().toDouble()
                            val fechaOriginal = editTextFecha.text.toString()
                            val descripcion = editTextDescripcion.text.toString()

                            val parts = fechaOriginal.split("/")
                            val fecha = "${parts[2]}-${parts[1]}-${parts[0]}"

                            // Implementar aquí la lógica para guardar los datos del nuevo ingreso
                            val nuevoIngreso= Ingreso(
                                descripcion = descripcion,
                                valor = cantidad,
                                fecha = fecha,
                                idUsuario = usuarioId,
                                tipo = "casual")
                            ingresoViewModel.insertIngreso(nuevoIngreso)

                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()
                }

                btnNuevoIngresoMes.setOnClickListener {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_ingreso_mes, null)
                    val editTextCantidad = dialogView.findViewById<TextInputEditText>(R.id.editTextCantidad)
                    val editTextFecha = dialogView.findViewById<EditText>(R.id.editTextFecha)
                    val editTextDescripcion = dialogView.findViewById<EditText>(R.id.editTextDescripcion)

                    editTextFecha.setOnClickListener {
                        showDatePickerDialog(editTextFecha)
                    }

                    val dialog = AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Guardar") { dialog, _ ->
                            val cantidad = editTextCantidad.text.toString().toDouble()
                            val fechaOriginal = editTextFecha.text.toString()
                            val descripcion = editTextDescripcion.text.toString()

                            val parts = fechaOriginal.split("/")
                            val fecha = "${parts[2]}-${parts[1]}-${parts[0]}"

                            // Implementar aquí la lógica para guardar los datos del nuevo ingreso
                            val nuevoIngreso= Ingreso(
                                descripcion = descripcion,
                                valor = cantidad,
                                fecha = fecha,
                                idUsuario = usuarioId,
                                tipo = "mensual")
                            ingresoViewModel.insertIngreso(nuevoIngreso)
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()
                }

                if (ingresosMensuales != null && ingresosCasuales != null) {
                    cargarIngresos(ingresosCasuales, contenedor_ingresos_cas)
                    cargarIngresos(ingresosMensuales, contenedor_ingresos_mes)
                }
            }
        })
    }


    private fun cargarIngresos(ingresos: List<Ingreso>, contenedor: ViewGroup) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 2
        for (ingreso in ingresos) {
            val descripcionTextView = TextView(requireContext())
            descripcionTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            descripcionTextView.text = ingreso.descripcion

            val valorTextView = TextView(requireContext())
            valorTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            valorTextView.text = "${numberFormat.format(ingreso.valor)}$"

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.END
            valorTextView.layoutParams = params

            valorTextView.setTextAppearance(requireContext(), R.style.TxtNegroMedianoItalic)

            val registroLayout = RelativeLayout(requireContext())
            registroLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            registroLayout.addView(descripcionTextView)
            descripcionTextView.setTextAppearance(requireContext(), R.style.TxtNegroMedianoItalic)

            val valorParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            valorParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            valorTextView.layoutParams = valorParams
            registroLayout.addView(valorTextView)

            contenedor.addView(registroLayout)
        }
    }

    private fun showDatePickerDialog(editTextFecha: EditText) {
        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear y mostrar el DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Aquí obtienes la fecha seleccionada y la estableces en el EditText
                val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
                editTextFecha.setText(fechaSeleccionada)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al enlace de datos para evitar fugas de memoria
        _binding = null
    }
}
