package com.example.coinbycoin

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coinbycoin.databinding.FragmentReporteBinding
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import android.graphics.Color as Color1

class Reporte : Fragment() {
    private var usuarioId: Long = -1
    private var _binding: FragmentReporteBinding? = null
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var gastosViewModel: GastosViewModel
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gastosViewModel = ViewModelProvider(this)[GastosViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        sharedViewModel.idUsuario.observe(viewLifecycleOwner) { usuarioId ->
            Log.d("FragmentGastos", "id usuario: $usuarioId")
            usuarioId?.let {
                this.usuarioId = it
                Log.d("FragmentReporte", "id usuario: $usuarioId")
                val adelanteSem = binding.btonAdelanteSem
                val atrasSem = binding.btonAtrasSem
                val adelanteMes = binding.btonAdelanteMes
                val atrasMes = binding.btonAtrasMes
                val adelanteAn= binding.btonAdelanteAn
                val atrasAn = binding.btonAtrasAn
                var fechaActual = LocalDate.now()
                Log.d("FragmentReporte", "fecha de referencia: $fechaActual")
                val categorias = listOf<String>("disponible", "Gastos Varios", "Alimentos", "Transporte", "Servicios", "Mercado")

                adelanteSem.setOnClickListener {
                    fechaActual = fechaActual.plusWeeks(1)
                    actualizarGrafico(fechaActual, "Semanal")
                }
                atrasSem.setOnClickListener {
                    fechaActual = fechaActual.minusWeeks(1)
                    actualizarGrafico(fechaActual, "Semanal")
                }
                adelanteMes.setOnClickListener {
                    fechaActual = fechaActual.plusMonths(1)
                    actualizarGrafico(fechaActual, "Mensual")
                }
                atrasMes.setOnClickListener {
                    fechaActual = fechaActual.minusMonths(1)
                    actualizarGrafico(fechaActual, "Mensual")
                }
                adelanteAn.setOnClickListener {
                    fechaActual = fechaActual.plusYears(1)
                    actualizarGrafico(fechaActual, "Anual")
                }
                atrasAn.setOnClickListener {
                    fechaActual = fechaActual.minusYears(1)
                    actualizarGrafico(fechaActual, "Anual")
                }
                actualizarGrafico(fechaActual,"Semanal")
                actualizarGrafico(fechaActual, "Mensual")
                actualizarGrafico(fechaActual, "Anual")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun actualizarGrafico(fechaActual: LocalDate, tiempo: String){
        Log.d("FragmentReporte", "fecha de referencia: $fechaActual, tiempo: $tiempo")
        lateinit var lineChartReporte: LineChartView
        when (tiempo){
            "Semanal" ->{
                lineChartReporte = binding.graficoLineasSemanal
                var fechaInf = periodoDeTiempo(tiempo, fechaActual).first.toString()
                var fechaSup = periodoDeTiempo(tiempo, fechaActual).second.toString()
                val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fechaInfFormateada  = fechaInf.format(formato)
                val fechaSupFormateada = fechaSup.format(formato)
                Log.d("FragmentReporte", "rango: $fechaInfFormateada a $fechaSupFormateada")
                gastosViewModel.getGastosPorFechas(usuarioId, fechaInfFormateada, fechaSupFormateada).observe(viewLifecycleOwner){listaSem ->
                    cargarGraficoLineasSem(lineChartReporte, convertirMapaALista(getDatosPorCategoria(listaSem)))
                    Log.d("FragmentReporte", "lista gastos: $listaSem")
                }
            }
            "Mensual" -> {
                lineChartReporte = binding.graficoLineasMensual
                var fechaInf = periodoDeTiempo(tiempo, fechaActual).first.toString()
                var fechaSup = periodoDeTiempo(tiempo, fechaActual).second.toString()
                val formato = DateTimeFormatter.ofPattern("yyyy-mm-dd")
                val fechaInfFormateada  = fechaInf.format(formato)
                val fechaSupFormateada = fechaSup.format(formato)
                Log.d("FragmentReporte", "rango: $fechaInfFormateada a $fechaSupFormateada")
                gastosViewModel.getGastosPorFechas(usuarioId, fechaInfFormateada, fechaSupFormateada).observe(viewLifecycleOwner){listaMes ->
                    cargarGraficoLineasMes(lineChartReporte, convertirMapaALista(getDatosPorCategoria(listaMes)))
                    Log.d("FragmentReporte", "lista gastos: $listaMes")
                }
            }
            "Anual" -> {
                lineChartReporte = binding.graficoLineasAnual
                var fechaInf = periodoDeTiempo(tiempo, fechaActual).first.toString()
                var fechaSup = periodoDeTiempo(tiempo, fechaActual).second.toString()
                val formato = DateTimeFormatter.ofPattern("yyyy-mm-dd")
                val fechaInfFormateada  = fechaInf.format(formato)
                val fechaSupFormateada = fechaSup.format(formato)
                Log.d("FragmentReporte", "rango: $fechaInfFormateada a $fechaSupFormateada")
                gastosViewModel.getGastosPorFechas(usuarioId, fechaInfFormateada, fechaSupFormateada).observe(viewLifecycleOwner){listaAn ->
                    Log.d("FragmentReporte", "lista gastos: $listaAn")
                    cargarGraficoLineasAn(lineChartReporte, convertirMapaALista(getDatosPorCategoria(listaAn)))
                }
            }
        }
    }

    private fun convertirMapaALista(datosPorCategoria: Map<String, Map<String, Double>>): Map<String, List<Pair<String, Double>>> {
        var listaDatosPorCategoria = mutableMapOf<String, List<Pair<String,Double>>>()

        for ((categoria, datos) in datosPorCategoria) {
            val listaDatos = datos.toList()
            listaDatosPorCategoria.put(categoria, listaDatos)
        }
        return listaDatosPorCategoria
    }
    private fun getDatosPorCategoria(lista: List<Gasto>): Map<String, Map<String, Double>> {
        val datosPorCategoria: MutableMap<String, MutableMap<String, Double>> = mutableMapOf()

        for (dato in lista) {
            val categoria = dato.categoria
            val fecha = dato.fecha
            val valor = dato.valor

            // Si la categoría no existe en el mapa, crear una nueva entrada
            if (!datosPorCategoria.containsKey(categoria)) {
                datosPorCategoria[categoria] = mutableMapOf(fecha to valor)
            } else {
                // Si la categoría existe, verificar si la fecha ya existe
                if (datosPorCategoria[categoria]?.containsKey(fecha) == true) {
                    // Si la fecha ya existe, sumar el valor al valor existente
                    val valorExistente = datosPorCategoria[categoria]?.get(fecha) ?: 0.0
                    datosPorCategoria[categoria]?.put(fecha, valorExistente + valor)
                } else {
                    // Si la fecha no existe, agregar una nueva entrada
                    datosPorCategoria[categoria]?.put(fecha, valor)
                }
            }
        }

        return datosPorCategoria
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun periodoDeTiempo(tiempo: String, fechaActual: LocalDate): Pair<LocalDate, LocalDate> {
        val fecha = fechaActual
        return when (tiempo) {
            "Semanal" -> {
                val inicioSemana = fechaActual.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val finSemana = fechaActual.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                Pair(inicioSemana, finSemana)
            }
            "Mensual" -> {
                val inicioMes = fechaActual.with(TemporalAdjusters.firstDayOfMonth())
                val finMes = fechaActual.with(TemporalAdjusters.lastDayOfMonth())
                Pair(inicioMes, finMes)
            }
            "Anual" -> {
                val inicioAnio = fechaActual.with(TemporalAdjusters.firstDayOfYear())
                val finAnio = fechaActual.with(TemporalAdjusters.lastDayOfYear())
                Pair(inicioAnio, finAnio)
            }
            else -> throw IllegalArgumentException("Tipo de rango de fechas no válido")
        }
    }

    fun cargarGraficoLineasSem(chartView: LineChartView, datosPorCategoria: Map<String, List<Pair<String, Double>>>) {
        val lineData = LineChartData()

        val lineas = mutableListOf<Line>() // Lista para almacenar todas las líneas

        for ((categoria, datos) in datosPorCategoria) {
            val line = Line()
            line.values = generarPuntos(datos)
            line.color = getColorCategoria(categoria)
            line.strokeWidth = 2
            line.pointRadius = 4
            line.isCubic = true
            lineas.add(line) // Agregar la línea a la lista
        }

        lineData.lines = lineas // Asignar la lista de líneas al LineChartData

        // Configuración del eje X (fecha)
        val axisX = Axis()
        // Configura aquí las etiquetas y otros aspectos del eje X

        // Configuración del eje Y (valor)
        val axisY = Axis()
        // Configura aquí las etiquetas y otros aspectos del eje Y

        lineData.axisXBottom = axisX
        lineData.axisYLeft = axisY

        chartView.lineChartData = lineData
    }
    fun cargarGraficoLineasMes(chartView: LineChartView, datosPorCategoria: Map<String, List<Pair<String, Double>>>) {
        val lineData1 = LineChartData()

        val lineas = mutableListOf<Line>() // Lista para almacenar todas las líneas

        for ((categoria, datos) in datosPorCategoria) {
            val line = Line()
            line.values = generarPuntos(datos)
            line.color = getColorCategoria(categoria)
            line.strokeWidth = 2
            line.pointRadius = 4
            line.isCubic = true
            lineas.add(line) // Agregar la línea a la lista
        }

        lineData1.lines = lineas // Asignar la lista de líneas al LineChartData

        // Configuración del eje X (fecha)
        val axisX = Axis()
        var formatter: SimpleAxisValueFormatter = SimpleAxisValueFormatter()


        // Configuración del eje Y (valor)
        val axisY = Axis()
        axisY.setMaxLabelChars(5)

        lineData1.axisXBottom = axisX
        lineData1.axisYLeft = axisY

        chartView.lineChartData = lineData1
    }

    fun cargarGraficoLineasAn(chartView: LineChartView, datosPorCategoria: Map<String, List<Pair<String, Double>>>) {
        val lineData2 = LineChartData()

        val lineas = mutableListOf<Line>() // Lista para almacenar todas las líneas

        for ((categoria, datos) in datosPorCategoria) {
            val line = Line()
            line.values = generarPuntos(datos)
            line.color = getColorCategoria(categoria)
            line.strokeWidth = 2
            line.pointRadius = 4
            line.isCubic = true
            lineas.add(line) // Agregar la línea a la lista
        }

        lineData2.lines = lineas // Asignar la lista de líneas al LineChartData

        // Configuración del eje X (fecha)
        val axisX = Axis()
        // Configura aquí las etiquetas y otros aspectos del eje X

        // Configuración del eje Y (valor)
        val axisY = Axis()
        // Configura aquí las etiquetas y otros aspectos del eje Y

        lineData2.axisXBottom = axisX
        lineData2.axisYLeft = axisY

        chartView.lineChartData = lineData2
    }


    // Función para generar los puntos a partir de los datos de fecha y valor
    fun generarPuntos(datos: List<Pair<String, Double>>): MutableList<PointValue> {
        val puntos = mutableListOf<PointValue>()
        // Iterar sobre los datos y asignar un índice a cada fecha
        var indice = 0
        val fechaIndiceMap = mutableMapOf<String, Int>()
        datos.forEach { (fecha, _) ->
            fechaIndiceMap[fecha] = indice
            indice++
        }
        // Generar los puntos con los valores de fecha y valor
        datos.forEach { (fecha, valor) ->
            val x = fechaIndiceMap[fecha]!!.toFloat() // Obtener el índice de la fecha
            val y = valor.toFloat()
            puntos.add(PointValue(x, y))
        }
        return puntos
    }
    fun getColorCategoria(categoria: String): Int {
        val categoriasColores = mapOf(
            "disponible" to "#87EE2B",
            "Gastos Varios" to "#F66B6B",
            "Alimentos" to "#FF66C1",
            "Transporte" to "#339AF0",
            "Servicios" to "#EEB62B",
            "Mercado" to "#FD8435"
        )
        val color = Color1.parseColor(categoriasColores[categoria])
        return color // Devuelve el color correspondiente a la categoría
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}