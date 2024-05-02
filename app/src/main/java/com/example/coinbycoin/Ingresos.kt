package com.example.coinbycoin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coinbycoin.databinding.FragmentIngresosBinding
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat

class Ingresos : Fragment(), IngresosListener {

    private var usuarioId: Long = -1
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var ingresoViewModel: IngresoViewModel
    private var ingresosMensuales: MutableList<Ingreso> = mutableListOf()
    private var ingresosCasuales: MutableList<Ingreso> = mutableListOf()
    private var totalIngresos :Double = 0.00
    private var _binding: FragmentIngresosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngresosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingresoViewModel = ViewModelProvider(this)[IngresoViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.idUsuario.observe(viewLifecycleOwner) { usuarioId ->
            Log.d("FragmentIngresos", "id usuario: $usuarioId")
            usuarioId?.let {
                this.usuarioId = it
                cargarIngresos()
            }
        }
        val btnNuevoIngresoMes = binding.btnNuevoIngresoMes
        val btnNuevoIngresoCas = binding.btnNuevoIngresoCas
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
                    val dia = parts[0].padStart(2, '0')
                    val mes = parts[1].padStart(2, '0')
                    val anio = parts[2]
                    val fecha = "${anio}-${mes}-${dia}"

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
                    val dia = parts[0].padStart(2, '0')
                    val mes = parts[1].padStart(2, '0')
                    val anio = parts[2]
                    val fecha = "${anio}-${mes}-${dia}"

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

    }

    private fun cargarIngresos() {
        ingresoViewModel.verificacion(usuarioId).observe(viewLifecycleOwner){ver ->
            Log.d("FragmentIngresos", "verificar $ver")
        }

        ingresoViewModel.getIngMesDeEsteMes(usuarioId).observe(viewLifecycleOwner) { ingMensuales ->
            ingresosMensuales = (ingMensuales.toMutableList())
            Log.d("FragmentIngresos", "ingresos Mensuales $ingresosMensuales")
            checkDataLoaded()
        }

        ingresoViewModel.getIngCasDeEsteMes(usuarioId).observe(viewLifecycleOwner) { ingCasuales ->
            ingresosCasuales = (ingCasuales.toMutableList())
            Log.d("FragmentIngresos", "ingresos Casuales $ingresosCasuales")
            checkDataLoaded()
        }

        ingresoViewModel.getIngTotalDeEsteMes(usuarioId).observe(viewLifecycleOwner) { ingTotal ->
            totalIngresos = ingTotal
            Log.d("FragmentIngresos", "total ingresos $totalIngresos")
            checkDataLoaded()
        }
    }

    private fun checkDataLoaded() {
        Log.d("FragmentIngresos", "Check datos cargados")
        if ((ingresosMensuales.isNotEmpty() || ingresosCasuales.isNotEmpty()) && totalIngresos != 0.00) {
            Log.d("FragmentIngresos", "datos cargados")
            onIngresosCargados(totalIngresos, ingresosMensuales, ingresosCasuales)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onIngresosCargados(totalIngresos: Double, ingresosMensuales: List<Ingreso>, ingresosCasuales: List<Ingreso>) {
        val numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 2
        binding.txtBlanco.text = "${numberFormat.format(totalIngresos)}$"

        val contenedorIngresosMes = binding.contenedorIngresosMes
        val contenedorIngresosCas = binding.contenedorIngresosCas

        Log.d("FragmentIngresos", "ingresos cargados")
        cargarIngresos(ingresosMensuales, contenedorIngresosMes)
        cargarIngresos(ingresosCasuales, contenedorIngresosCas)
    }

    @SuppressLint("SetTextI18n")
    private fun cargarIngresos(ingresos: List<Ingreso>, contenedor: ViewGroup) {
        Log.d("FragmentIngresos", "cargando contenedor ${contenedor.id}")
        // Limpiar el contenedor antes de cargar los nuevos ingresos
        contenedor.removeAllViews()
        val numberFormat = NumberFormat.getInstance()
        numberFormat.maximumFractionDigits = 2
        for (ingreso in ingresos) {
            val descripcionTextView = TextView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text = ingreso.descripcion
                setTextAppearance(R.style.TxtNegroMedianoItalic)
            }

            val valorTextView = TextView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text = "${numberFormat.format(ingreso.valor)}$"
                setTextAppearance(R.style.TxtNegroMedianoItalic)
            }

            val registroLayout = ConstraintLayout(requireContext()).apply {
                layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                addView(descripcionTextView)
                addView(valorTextView)

                val descParams = descripcionTextView.layoutParams as ConstraintLayout.LayoutParams
                descParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                descParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

                val valorParams = valorTextView.layoutParams as ConstraintLayout.LayoutParams
                valorParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                valorParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

                contenedor.addView(this)
            }
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
