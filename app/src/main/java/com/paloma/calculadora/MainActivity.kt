package com.paloma.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.paloma.calculadora.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.round

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    // Estado de la calculadora
    private var primerNumero = 0.0
    private var operacion = 0
    private var empezarNumeroNuevo = true
    private val maximoDigitos = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // Números
        binding.btnCero.setOnClickListener(this)
        binding.btnUno.setOnClickListener(this)
        binding.btnDos.setOnClickListener(this)
        binding.btnTres.setOnClickListener(this)
        binding.btnCuatro.setOnClickListener(this)
        binding.btnCinco.setOnClickListener(this)
        binding.btnSeis.setOnClickListener(this)
        binding.btnSiete.setOnClickListener(this)
        binding.btnOcho.setOnClickListener(this)
        binding.btnNueve.setOnClickListener(this)
        binding.btnPunto.setOnClickListener(this)
        // Operaciones
        binding.btnSuma.setOnClickListener(this)
        binding.btnResta.setOnClickListener(this)
        binding.btnMultiplicacion.setOnClickListener(this)
        binding.btnDivision.setOnClickListener(this)
        binding.btnIgual.setOnClickListener(this)
        // Funciones
        binding.btnLimpiar.setOnClickListener(this)
        binding.btnBorrar.setOnClickListener(this)
        binding.btnCambioSigno.setOnClickListener(this)
        binding.btnPorcentaje.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            // Números: escribimos el texto del botón pulsado
            binding.btnCero.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnUno.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnDos.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnTres.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnCuatro.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnCinco.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnSeis.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnSiete.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnOcho.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnNueve.id -> escribirNumero((p0 as Button).text.toString())
            binding.btnPunto.id -> escribirPunto()

            // Operaciones: guardamos qué botón se ha pulsado y su símbolo
            binding.btnSuma.id -> elegirOperacion(p0.id, (p0 as Button).text.toString())
            binding.btnResta.id -> elegirOperacion(p0.id, (p0 as Button).text.toString())
            binding.btnMultiplicacion.id -> elegirOperacion(p0.id, (p0 as Button).text.toString())
            binding.btnDivision.id -> elegirOperacion(p0.id, (p0 as Button).text.toString())
            binding.btnIgual.id -> calcular()

            // Funciones
            binding.btnLimpiar.id -> limpiar()
            binding.btnBorrar.id -> borrar()
            binding.btnCambioSigno.id -> cambiarSigno()
            binding.btnPorcentaje.id -> porcentaje()
        }
    }

    // ---------- ESCRITURA ----------

    private fun escribirNumero(digito: String) {
        val pantalla = binding.textResultado.text.toString()
        if (empezarNumeroNuevo || pantalla == resources.getString(R.string.cero)) {
            binding.textResultado.text = digito
            empezarNumeroNuevo = false
        } else if (pantalla.length < maximoDigitos) {
            binding.textResultado.text = "$pantalla$digito"
        }
    }

    private fun escribirPunto() {
        val punto = resources.getString(R.string.punto)
        val pantalla = binding.textResultado.text.toString()
        if (empezarNumeroNuevo) {
            binding.textResultado.text = "${resources.getString(R.string.cero)}$punto"
            empezarNumeroNuevo = false
        } else if (!pantalla.contains(punto)) {
            binding.textResultado.text = "$pantalla$punto"
        }
    }

    // ---------- OPERACIONES ----------

    private fun elegirOperacion(idOperacion: Int, simbolo: String) {
        // Si ya había una operación y se ha escrito el segundo número, la resolvemos primero (2 + 3 + ...)
        if (operacion != 0 && !empezarNumeroNuevo) {
            calcular()
        }
        val numero = leerPantalla() ?: return
        primerNumero = numero
        operacion = idOperacion
        binding.textOperacion.text = "${formatear(numero)} $simbolo"
        empezarNumeroNuevo = true
    }

    private fun calcular() {
        if (operacion == 0) return
        val segundoNumero = leerPantalla() ?: return

        if (operacion == binding.btnDivision.id && segundoNumero == 0.0) {
            mostrarAviso(R.string.snack_division_cero)
            mostrarError()
            return
        }

        val resultado = when (operacion) {
            binding.btnSuma.id -> primerNumero + segundoNumero
            binding.btnResta.id -> primerNumero - segundoNumero
            binding.btnMultiplicacion.id -> primerNumero * segundoNumero
            binding.btnDivision.id -> primerNumero / segundoNumero
            else -> segundoNumero
        }

        binding.textOperacion.text =
            "${binding.textOperacion.text} ${formatear(segundoNumero)} ${resources.getString(R.string.igual)}"
        operacion = 0
        mostrarResultado(resultado)
    }

    // ---------- FUNCIONES ----------

    private fun limpiar() {
        binding.textResultado.text = resources.getString(R.string.pantalla_inicial)
        binding.textOperacion.text = ""
        primerNumero = 0.0
        operacion = 0
        empezarNumeroNuevo = true
    }

    private fun borrar() {
        if (empezarNumeroNuevo) return
        val pantalla = binding.textResultado.text.toString()
        val nuevoTexto = pantalla.substring(0, pantalla.length - 1)
        if (nuevoTexto.toDoubleOrNull() == null) {
            binding.textResultado.text = resources.getString(R.string.pantalla_inicial)
            empezarNumeroNuevo = true
        } else {
            binding.textResultado.text = nuevoTexto
        }
    }

    private fun cambiarSigno() {
        val numero = leerPantalla() ?: return
        if (numero == 0.0) return
        binding.textResultado.text = formatear(-numero)
    }

    private fun porcentaje() {
        val numero = leerPantalla() ?: return
        mostrarResultado(numero / 100)
    }

    // ---------- AYUDAS ----------

    private fun leerPantalla(): Double? = binding.textResultado.text.toString().toDoubleOrNull()

    private fun mostrarResultado(numero: Double) {
        binding.textResultado.text = formatear(numero)
        empezarNumeroNuevo = true
    }

    private fun formatear(numero: Double): String {
        // Redondeamos a 9 decimales para evitar cosas como 0.1 + 0.2 = 0.30000000000000004
        val redondeado = round(numero * 1000000000) / 1000000000
        // Si es entero, lo mostramos sin ".0"
        return if (redondeado % 1 == 0.0 && abs(redondeado) < 1e15) {
            redondeado.toLong().toString()
        } else {
            redondeado.toString()
        }
    }

    private fun mostrarError() {
        binding.textResultado.text = resources.getString(R.string.error)
        binding.textOperacion.text = ""
        operacion = 0
        empezarNumeroNuevo = true
    }

    private fun mostrarAviso(idTexto: Int) {
        Snackbar.make(binding.root, resources.getString(idTexto), Snackbar.LENGTH_LONG).show()
    }
}