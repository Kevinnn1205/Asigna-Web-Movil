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
import java.text.SimpleDateFormat
import java.util.*

class Crearreserva : Fragment() {

    private lateinit var txtNombre_completo: EditText
    private lateinit var txtusername: EditText  // Campo para el correo electrónico
    private lateinit var txtNombre_espacio: Spinner
    private lateinit var txtFecha_entrada: EditText
    private lateinit var txtFecha_salida: EditText
    private lateinit var txtHora_entrada: EditText
    private lateinit var txtHora_salida: EditText
    private lateinit var btnGuardar: Button

    private var fechaEntrada: Calendar? = null
    private var fechaSalida: Calendar? = null
    private var horaEntrada: Calendar? = null
    private var horaSalida: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crearreserva, container, false)

        // Inicializar las referencias de los componentes de la interfaz
        txtNombre_completo = view.findViewById(R.id.txtNombre_completo)
        txtusername = view.findViewById(R.id.txtusername)  // Inicialización del nuevo campo
        txtFecha_entrada = view.findViewById(R.id.txtFecha_entrada)
        txtFecha_salida = view.findViewById(R.id.txtFecha_salida)
        txtHora_entrada = view.findViewById(R.id.txtHora_entrada)
        txtHora_salida = view.findViewById(R.id.txtHora_salida)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicializar los botones de los calendarios
        val btnCalendario = view.findViewById<ImageView>(R.id.btnCalendario)
        val btnCalendario2 = view.findViewById<ImageView>(R.id.btnCalendario2)

        // Configurar el Spinner con las opciones
        txtNombre_espacio = view.findViewById(R.id.SpinnerNombreEspacio)
        val opciones = arrayOf("cancha", "gimnasio", "auditorio", "biblioteca")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        txtNombre_espacio.adapter = adapter

        // Asignar listeners a los botones de calendario y de selección de hora
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

        // Asignar listener al botón de guardar
        btnGuardar.setOnClickListener {
            crearReserva()
        }

        return view
    }

    fun crearReserva() {
        try {
            // Verifica que los campos no estén vacíos
            if (txtNombre_completo.text.isEmpty() ||
                txtusername.text.isEmpty() ||  // Verificar si el campo de username está vacío
                txtFecha_entrada.text.isEmpty() ||
                txtFecha_salida.text.isEmpty() ||
                txtHora_entrada.text.isEmpty() ||
                txtHora_salida.text.isEmpty()) {
                Toast.makeText(context, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return
            }

            // Verifica que la fecha y hora de salida no sea anterior a la de entrada
            if (fechaEntrada != null && fechaSalida != null && horaEntrada != null && horaSalida != null) {
                val fechaHoraEntrada = Calendar.getInstance().apply {
                    time = fechaEntrada!!.time
                    set(Calendar.HOUR_OF_DAY, horaEntrada!!.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, horaEntrada!!.get(Calendar.MINUTE))
                }

                val fechaHoraSalida = Calendar.getInstance().apply {
                    time = fechaSalida!!.time
                    set(Calendar.HOUR_OF_DAY, horaSalida!!.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, horaSalida!!.get(Calendar.MINUTE))
                }

                if (fechaHoraSalida.before(fechaHoraEntrada)) {
                    Toast.makeText(context, "La hora de salida no puede ser anterior a la de entrada.", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            // Crear el objeto JSON con los parámetros a enviar
            val parametros = JSONObject().apply {
                put("nombre_completo", txtNombre_completo.text.toString())
                put("username", txtusername.text.toString())  // Agregar el campo de username
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
                    Toast.makeText(context, "Reserva creada con éxito", Toast.LENGTH_LONG).show()
                },
                { error ->
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

                if (esFechaEntrada) {
                    fechaEntrada = selectedDate
                } else {
                    fechaSalida = selectedDate
                }
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

            if (esHoraEntrada) {
                horaEntrada = selectedTime
            } else {
                horaSalida = selectedTime
            }
        }, currentHour, currentMinute, true)

        timePickerDialog.show()
    }
}
