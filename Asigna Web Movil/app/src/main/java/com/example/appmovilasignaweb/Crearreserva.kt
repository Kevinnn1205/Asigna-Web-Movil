package com.example.appmovilasignaweb

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Crearreserva : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var txtNombre_completo: EditText
    private lateinit var txtNombre_espacio: EditText
    private lateinit var txtFecha_entrada: EditText
    private lateinit var txtFecha_salida: EditText
    private lateinit var txtHora_entrada: EditText
    private lateinit var txtHora_salida: EditText

    private lateinit var btnGuardar: Button

    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun crearReserva() {
        try {
            // Verifica que los campos no estén vacíos
            if (txtNombre_completo.text.isEmpty() ||
                txtNombre_espacio.text.isEmpty() ||
                txtFecha_entrada.text.isEmpty() ||
                txtFecha_salida.text.isEmpty() ||
                txtHora_entrada.text.isEmpty() ||
                txtHora_salida.text.isEmpty()) {
                Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return
            }

            val parametros = JSONObject().apply {
                put("nombre_completo", txtNombre_completo.text.toString())
                put("nombre_espacio", txtNombre_espacio.text.toString())
                put("fecha_entrada", txtFecha_entrada.text.toString())
                put("fecha_salida", txtFecha_salida.text.toString())
                put("hora_entrada", txtHora_entrada.text.toString())
                put("hora_salida", txtHora_salida.text.toString())
            }

            // Realiza la solicitud POST al backend
            val request = JsonObjectRequest(
                Request.Method.POST,
                config.urlcrearReserva, // Asegúrate que esta URL esté correcta
                parametros,
                { response ->
                    // Maneja la respuesta del backend
                    Toast.makeText(context, "Reserva creada con éxito: ${response.getString("mensaje")}", Toast.LENGTH_LONG).show()
                },
                { error ->
                    Toast.makeText(context, "Error al crear la reserva: ${error.message}", Toast.LENGTH_LONG).show()
                    Log.e("Crearreserva", "Error en la solicitud: ${error.message}")
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        } catch (error: Exception) {
            Log.e("Crearreserva", "Error al crear la reserva: ${error.message}")
            error.printStackTrace()
        }
    }

    fun mostrarCalendario(text: EditText, esFechaEntrada: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }

            if (selectedDate.before(Calendar.getInstance())) {
                Toast.makeText(requireContext(), "No puedes seleccionar una fecha anterior a la actual.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                text.setText(formattedDate)
            }
        }, year, month, day)

        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    fun mostrarHora(text: EditText, esHoraEntrada: Boolean) {
        try {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }

                if (esHoraEntrada) {
                    // Validar que la hora de entrada no sea anterior a la actual
                    if (selectedTime.before(Calendar.getInstance())) {
                        Toast.makeText(requireContext(), "No puedes seleccionar una hora anterior a la actual.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Formatear y establecer la hora de entrada
                        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                        text.setText(formattedTime)
                    }
                } else {
                    // Validar que la hora de salida no sea anterior a la hora de entrada
                    val horaEntradaStr = txtHora_entrada.text.toString()
                    if (horaEntradaStr.isEmpty()) {
                        Toast.makeText(requireContext(), "Primero selecciona la hora de entrada.", Toast.LENGTH_SHORT).show()
                        return@TimePickerDialog
                    }

                    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                    try {
                        val horaEntrada = sdf.parse(horaEntradaStr)
                        if (horaEntrada != null) {
                            val horaSalida = sdf.parse(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute))
                            if (horaSalida != null && horaSalida.before(horaEntrada)) {
                                Toast.makeText(requireContext(), "La hora de salida no puede ser anterior a la hora de entrada.", Toast.LENGTH_SHORT).show()
                                return@TimePickerDialog
                            }
                        }
                    } catch (e: ParseException) {
                        Toast.makeText(requireContext(), "Error al procesar las horas.", Toast.LENGTH_SHORT).show()
                        Log.e("Crearreserva", "Error al parsear las horas: ${e.message}")
                        return@TimePickerDialog
                    }

                    // Formatear y establecer la hora de salida
                    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                    text.setText(formattedTime)
                }
            }, currentHour, currentMinute, true)

            timePickerDialog.show()

        } catch (e: Exception) {
            Log.e("Crearreserva", "Error en TimePickerDialog: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crearreserva, container, false)

        // Obtener las referencias de los botones y EditText
        val btnCalendario = view.findViewById<ImageView>(R.id.btnCalendario)
        val btnCalendario2 = view.findViewById<ImageView>(R.id.btnCalendario2)
        txtFecha_entrada = view.findViewById(R.id.txtFecha_entrada)
        txtFecha_salida = view.findViewById(R.id.txtFecha_salida)

        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Nuevas referencias para los campos de hora
        txtHora_entrada = view.findViewById(R.id.txtHora_entrada)
        txtHora_salida = view.findViewById(R.id.txtHora_salida)

        // Configurar el Spinner
        val spinner: Spinner = view.findViewById(R.id.SpinnerNombreEspacio)
        val opciones = arrayOf("cancha", "gimnasio", "auditorio", "biblioteca")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Asignar los listeners de clic a los botones
        btnCalendario.setOnClickListener {
            mostrarCalendario(txtFecha_entrada, true)
        }

        btnCalendario2.setOnClickListener {
            mostrarCalendario(txtFecha_salida, false)
        }

        // Asignar los listeners de clic a los campos de hora
        txtHora_entrada.setOnClickListener {
            if (::txtHora_entrada.isInitialized) {
                mostrarHora(txtHora_entrada, true)
            } else {
                Log.e("Crearreserva", "txtHora_entrada no está inicializado")
            }
        }

        txtHora_salida.setOnClickListener {
            if (::txtHora_salida.isInitialized) {
                mostrarHora(txtHora_salida, false)
            } else {
                Log.e("Crearreserva", "txtHora_salida no está inicializado")
            }
        }

        btnGuardar.setOnClickListener {
            crearReserva()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Crearreserva().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
