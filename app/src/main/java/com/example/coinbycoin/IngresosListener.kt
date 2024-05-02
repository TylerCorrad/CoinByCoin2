package com.example.coinbycoin
interface IngresosListener {
    fun onIngresosCargados(totalIngresos: Double, ingresosMensuales: List<Ingreso>, ingresosCasuales: List<Ingreso>)
}
