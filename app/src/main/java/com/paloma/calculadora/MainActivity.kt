package com.paloma.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.paloma.calculadora.databinding.ActivityMainBinding
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

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

    // ---------- FASE 6: MANTENER EL ESTADO AL GIRAR ----------

    override fun onSaveInstanceState(estado: Bundle) {
        super.onSaveInstanceState(estado)
        // Guardamos el estado antes de que la actividad se destruya
        estado.putString("RESULTADO", binding.textResultado.text.toString())
        estado.putString("OPERACION_TEXTO", binding.textOperacion.text.toString())
        estado.putDouble("PRIMER_NUMERO", primerNumero)
        estado.putInt("OPERACION", operacion)
        estado.putBoolean("NUMERO_NUEVO", empezarNumeroNuevo)
    }

    override fun onRestoreInstanceState(estado: Bundle) {
        super.onRestoreInstanceState(estado)
        // Recuperamos el estado al volver a crear la actividad
        binding.textResultado.text = estado.getString("RESULTADO")
        binding.textOperacion.text = estado.getString("OPERACION_TEXTO")
        primerNumero = estado.getDouble("PRIMER_NUMERO")
        operacion = estado.getInt("OPERACION")
        empezarNumeroNuevo = estado.getBoolean("NUMERO_NUEVO")
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
        // FASE 5: científicos (solo existen en horizontal -> null safety con ?.)
        binding.btnSeno?.setOnClickListener(this)
        binding.btnCoseno?.setOnClickListener(this)
        binding.btnTangente?.setOnClickListener(this)
        binding.btnLogaritmoNeperiano?.setOnClickListener(this)
        binding.btnLogaritmo?.setOnClickListener(this)
        binding.btnRaiz?.setOnClickListener(this)
        binding.btnCuadrado?.setOnClickListener(this)
        binding.btnCubo?.setOnClickListener(this)
        binding.btnPotencia?.setOnClickListener(this)
        binding.btnInverso?.setOnClickListener(this)
        binding.btnFactorial?.setOnClickListener(this)
        binding.btnDiezElevado?.setOnClickListener(this)
        binding.btnPi?.setOnClickListener(this)
        binding.btnNumeroE?.setOnClickListener(this)
        binding.btnEElevado?.setOnClickListener(this)
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

            // FASE 5: científicos
            binding.btnPotencia?.id -> elegirOperacion((p0 as Button).id, resources.getString(R.string.simbolo_potencia))
            binding.btnPi?.id -> mostrarResultado(PI)
            binding.btnNumeroE?.id -> mostrarResultado(E)
            binding.btnSeno?.id -> operacionCientifica(p0 as Button)
            binding.btnCoseno?.id -> operacionCientifica(p0 as Button)
            binding.btnTangente?.id -> operacionCientifica(p0 as Button)
            binding.btnLogaritmoNeperiano?.id -> operacionCientifica(p0 as Button)
            binding.btnLogaritmo?.id -> operacionCientifica(p0 as Button)
            binding.btnRaiz?.id -> operacionCientifica(p0 as Button)
            binding.btnCuadrado?.id -> operacionCientifica(p0 as Button)
            binding.btnCubo?.id -> operacionCientifica(p0 as Button)
            binding.btnInverso?.id -> operacionCientifica(p0 as Button)
            binding.btnFactorial?.id -> operacionCientifica(p0 as Button)
            binding.btnDiezElevado?.id -> operacionCientifica(p0 as Button)
            binding.btnEElevado?.id -> operacionCientifica(p0 as Button)
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

    // ---------- OPERACIONES DE DOS NÚMEROS ----------

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

        // FASE 5: un número negativo elevado a un decimal no existe, ej. (-8)^0.5
        if (operacion == binding.btnPotencia?.id && primerNumero < 0 && segundoNumero % 1 != 0.0) {
            mostrarAviso(R.string.snack_operacion_invalida)
            mostrarError()
            return
        }

        val resultado = when (operacion) {
            binding.btnSuma.id -> primerNumero + segundoNumero
            binding.btnResta.id -> primerNumero - segundoNumero
            binding.btnMultiplicacion.id -> primerNumero * segundoNumero
            binding.btnDivision.id -> primerNumero / segundoNumero
            binding.btnPotencia?.id -> primerNumero.pow(segundoNumero)
            else -> segundoNumero
        }

        binding.textOperacion.text =
            "${binding.textOperacion.text} ${formatear(segundoNumero)} ${resources.getString(R.string.igual)}"
        operacion = 0
        mostrarResultado(resultado)
    }

    // ---------- FASE 5: OPERACIONES DE UN NÚMERO ----------

    private fun operacionCientifica(boton: Button) {
        val idBoton = boton.id
        val nombre = boton.text.toString()
        val numero = leerPantalla() ?: return

        // 1. Comprobamos los casos imposibles antes de calcular
        val aviso: Int? = when (idBoton) {
            binding.btnRaiz?.id -> if (numero < 0) R.string.snack_raiz_negativa else null
            binding.btnLogaritmo?.id -> if (numero <= 0) R.string.snack_logaritmo else null
            binding.btnLogaritmoNeperiano?.id -> if (numero <= 0) R.string.snack_logaritmo else null
            binding.btnInverso?.id -> if (numero == 0.0) R.string.snack_division_cero else null
            binding.btnFactorial?.id ->
                if (numero < 0 || numero > 170 || numero % 1 != 0.0) R.string.snack_factorial else null
            binding.btnTangente?.id -> if (abs(numero % 180) == 90.0) R.string.snack_operacion_invalida else null
            else -> null
        }
        if (aviso != null) {
            mostrarAviso(aviso)
            mostrarError()
            return
        }

        // 2. Calculamos (sin, cos y tan en grados: pasamos a radianes con * PI / 180)
        val resultado = when (idBoton) {
            binding.btnSeno?.id -> sin(numero * PI / 180)
            binding.btnCoseno?.id -> cos(numero * PI / 180)
            binding.btnTangente?.id -> tan(numero * PI / 180)
            binding.btnLogaritmoNeperiano?.id -> ln(numero)
            binding.btnLogaritmo?.id -> log10(numero)
            binding.btnRaiz?.id -> sqrt(numero)
            binding.btnCuadrado?.id -> numero * numero
            binding.btnCubo?.id -> numero * numero * numero
            binding.btnInverso?.id -> 1 / numero
            binding.btnFactorial?.id -> factorial(numero.toInt())
            binding.btnDiezElevado?.id -> 10.0.pow(numero)
            binding.btnEElevado?.id -> E.pow(numero)
            else -> numero
        }

        // 3. Mostramos la operación arriba (solo si no hay otra operación pendiente)
        if (operacion == 0) {
            binding.textOperacion.text = "$nombre(${formatear(numero)}) ${resources.getString(R.string.igual)}"
        }
        mostrarResultado(resultado)
    }

    private fun factorial(n: Int): Double {
        var resultado = 1.0
        for (i in 1..n) {
            resultado *= i
        }
        return resultado
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
        // FASE 5: si el resultado no cabe en un Double (ej. 10^400), es un error
        if (abs(numero) > 1e308) {
            mostrarAviso(R.string.snack_operacion_invalida)
            mostrarError()
            return
        }
        binding.textResultado.text = formatear(numero)
        empezarNumeroNuevo = true
    }

    private fun formatear(numero: Double): String {
        // Los números enormes los dejamos tal cual (se verán con E, ej. 1.0E20)
        if (abs(numero) >= 1e15) return numero.toString()
        // Redondeamos a 9 decimales para evitar cosas como 0.1 + 0.2 = 0.30000000000000004
        val redondeado = round(numero * 1000000000) / 1000000000
        // Si es entero, lo mostramos sin ".0"
        return if (redondeado % 1 == 0.0) {
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