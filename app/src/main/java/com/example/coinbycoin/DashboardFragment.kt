package com.example.coinbycoin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinbycoin.databinding.FragmentDashboardBinding
import com.example.coinbycoin.ui.theme.CustomSpinnerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat

class DashboardFragment : Fragment() {

    private var usuarioId: Long = -1
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var gastosViewModel: GastosViewModel
    private var disponible: Double = 0.0
    private var total: Double = 0.0
    private var gastados: Double = 0.0

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gastosViewModel = ViewModelProvider(this)[GastosViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.idUsuario.observe(viewLifecycleOwner) { usuarioId ->
            Log.d("FragmentGastos", "id usuario: $usuarioId")
            usuarioId?.let {
                this.usuarioId = it
                cargarDatos()
                val bloqueTransporte = binding.bloqueTransporte
                val bloqueGastosVarios = binding.bloqueGastosVarios
                val bloqueMercado = binding.bloqueMercado
                val bloqueServicios = binding.bloqueServicios
                val bloqueAlimentos = binding.bloqueAlimentos
                val recyclrerViewTransporte = binding.recyclerViewTransporte
                val recyclrerViewGastosVarios = binding.recyclerViewGastosVarios
                val recyclrerViewMercado = binding.recyclerViewMercado
                val recyclrerViewServicios = binding.recyclerViewServicios
                val recyclrerViewAlimentos = binding.recyclerViewAlimentos


                bloqueTransporte.setOnClickListener{
                    var visibilidad = false
                    mostrarListaDeGastos(recyclrerViewTransporte, "Transporte")
                }
                bloqueMercado.setOnClickListener{
                    mostrarListaDeGastos(recyclrerViewMercado, "Mercado")
                }
                bloqueServicios.setOnClickListener{
                    mostrarListaDeGastos(recyclrerViewServicios, "Servicios")
                }
                bloqueAlimentos.setOnClickListener{
                    mostrarListaDeGastos(recyclrerViewAlimentos, "Alimentos")
                }
                bloqueGastosVarios.setOnClickListener{
                    mostrarListaDeGastos(recyclrerViewGastosVarios, "Gastos Varios")
                }


            }
        }
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

            editTextFecha.setOnClickListener {
                showDatePickerDialog(editTextFecha)
            }

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Guardar") { dialog, _ ->
                    val categoria = spinnerCategoria.selectedItem.toString()
                    val cantidad = editTextCantidad.text.toString()
                    val fechaOriginal = editTextFecha.text.toString()
                    val descripcion = editTextDescripcion.text.toString()

                    val parts = fechaOriginal.split("/")
                    val dia = parts[0].padStart(2, '0')
                    val mes = parts[1].padStart(2, '0')
                    val anio = parts[2]
                    val fecha = "${anio}-${mes}-${dia}"

                    val nuevoGasto = Gasto(
                        categoria = categoria,
                        fecha = fecha,
                        valor = cantidad.toDouble(),
                        descripcion = descripcion,
                        idUsuario = usuarioId
                    )
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            gastosViewModel.insertGasto(nuevoGasto)
                        }
                    }
                        }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
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

    @SuppressLint("SetTextI18n")
    private fun cargarDatos(){
        gastosViewModel.getDisponible(usuarioId).observe(viewLifecycleOwner){ disp ->
            val numberFormat = NumberFormat.getInstance()
            numberFormat.maximumFractionDigits = 2
            if(disp != null) {
                disponible = disp
                val disponibleTextView = binding.cantidadDisponible
                disponibleTextView.setText("${numberFormat.format(disponible)}$")
                //cargar la barra de disponible
            }
        }
        gastosViewModel.getValorGastosMesCategoria(usuarioId, "Gastos Varios").observe(viewLifecycleOwner){cantidad ->
            if (cantidad != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadCategoria = cantidad
                val gastosVariosTextView = binding.cantidadGastosVarios
                gastosVariosTextView.setText("${numberFormat.format(cantidadCategoria)}$")
                //cargar la barra de gastos varios
            }
        }

        gastosViewModel.getValorGastosMesCategoria(usuarioId, "Alimentos").observe(viewLifecycleOwner){cantidad ->
            if (cantidad != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadCategoria = cantidad
                val AlimentosTextView = binding.cantidadAlimentos
                AlimentosTextView.setText("${numberFormat.format(cantidadCategoria)}$")
                //cargar la barra de alimentos
            }
        }

        gastosViewModel.getValorGastosMesCategoria(usuarioId, "Transporte").observe(viewLifecycleOwner){cantidad ->
            if (cantidad != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadCategoria = cantidad
                val TransporteTextView = binding.cantidadTransporte
                TransporteTextView.setText("${numberFormat.format(cantidadCategoria)}$")
                //cargar la barra de Transporte
            }
        }

        gastosViewModel.getValorGastosMesCategoria(usuarioId, "Servicios").observe(viewLifecycleOwner){cantidad ->
            if (cantidad != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadCategoria = cantidad
                val ServiciosTextView = binding.cantidadServicios
                ServiciosTextView.setText("${numberFormat.format(cantidadCategoria)}$")
                //cargar la barra de Servicios
            }
        }

        gastosViewModel.getValorGastosMesCategoria(usuarioId, "Mercado").observe(viewLifecycleOwner){cantidad ->
            if (cantidad != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadCategoria = cantidad
                val MercadoTextView = binding.cantidadMercado
                MercadoTextView.setText("${numberFormat.format(cantidadCategoria)}$")
                //cargar la barra de Mercado
            }
        }

        gastosViewModel.getValorGastosMes(usuarioId).observe(viewLifecycleOwner){gastosMes ->
            if (gastosMes != null) {
                val numberFormat = NumberFormat.getInstance()
                numberFormat.maximumFractionDigits = 2
                val cantidadGastos = gastosMes
                val gastadosTextView = binding.TxtGastoTotal
                gastadosTextView.setText("${numberFormat.format(cantidadGastos)}$")
                //cargar circulo general
            }
        }
    }

    private fun mostrarListaDeGastos(recyclerView: RecyclerView, categoria: String){
        gastosViewModel.getGastosMesCategoria(usuarioId, categoria).observe(viewLifecycleOwner){gastosCat ->
            // Crear un adaptador para el RecyclerView
            val adapter = GastoAdapter(gastosCat)

            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setAdapter(adapter)
            }
            if(recyclerView.visibility == View.GONE){
                recyclerView.visibility = View.VISIBLE
            }else{
                recyclerView.visibility = View.GONE
            }

        }
    }
}