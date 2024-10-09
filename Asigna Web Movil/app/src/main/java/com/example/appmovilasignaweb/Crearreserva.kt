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

class Crearreserva : Fragment() {

    private lateinit var txtNombre_completo: EditText
    private lateinit var txtNombre_espacio: Spinner
    private lateinit var txtFecha_entrada: EditText
    private lateinit var txtFecha_salida: EditText
    private lateinit var txtHora_entrada: EditText
    private lateinit var txtHora_salida: EditText
    private lateinit var btnGuardar: Button

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
        txtHora_entrada = view.findViewById(R.id.txtHora_entrada)
        txtHora_salida = view.findViewById(R.id.txtHora_salida)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Configurar el Spinner
        txtNombre_espacio = view.findViewById(R.id.SpinnerNombreEspacio)
        val opciones = arrayOf("cancha", "gimnasio", "auditorio", "biblioteca")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        txtNombre_espacio.adapter = adapter

        // Asignar los listeners de clic a los botones
        btnCalendario.setOnClickListener {
            mostrarCalendario(txtFecha_entrada, true)
        }

        btnCalendario2.setOnClickListener {
            mostrarCalendario(txtFecha_salida, false)
        }

        txtHora_entrada.setOnClickListener {
            mostrarHora(txtHora_entrada, true)
        }

        txtHora_salida.setOnClickListener {
            mostrarHora(txtHora_salida, false)
        }

        btnGuardar.setOnClickListener {
            crearReserva()
        }

        return view
    }

    fun crearReserva() {
        try {
            // Verifica que los campos no estén vacíos
            if (txtNombre_completo.text.isEmpty() ||
                txtFecha_entrada.text.isEmpty() ||
                txtFecha_salida.text.isEmpty() ||
                txtHora_entrada.text.isEmpty() ||
                txtHora_salida.text.isEmpty()) {
                Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return
            }

            // Crear el objeto JSON con los parámetros a enviar
            val parametros = JSONObject().apply {
                put("nombre_completo", txtNombre_completo.text.toString())
                put("nombre_espacio", txtNombre_espacio.selectedItem.toString())
                put("fecha_entrada", txtFecha_entrada.text.toString())
                put("fecha_salida", txtFecha_salida.text.toString())
                put("hora_entrada", txtHora_entrada.text.toString())
                put("hora_salida", txtHora_salida.text.toString())
            }

            // Realizar la solicitud POST al backend
            val request = JsonObjectRequest(
                Request.Method.POST,
                config.urlcrearReserva,  // Aquí va la URL del endpoint de backend
                parametros,
                { response ->
                    // Manejar la respuesta exitosa del backend
                    Toast.makeText(context, "Reserva creada con éxito", Toast.LENGTH_LONG).show()
                },
                { error ->
                    // Manejar el error
                    error.networkResponse?.let {
                        val statusCode = it.statusCode
                        if (statusCode == 400) {
                            val errorResponse = String(it.data)
                            Toast.makeText(context, "Error: $errorResponse", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Error al crear la reserva", Toast.LENGTH_LONG).show()
                        }
                    } ?: run {
                        Toast.makeText(context, "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                    Log.e("Crearreserva", "Error en la solicitud: ${error.message}")
                }
            )

            // Agregar la solicitud a la cola de peticiones
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
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }

            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            text.setText(formattedTime)
        }, currentHour, currentMinute, true)

        timePickerDialog.show()
    }
}
