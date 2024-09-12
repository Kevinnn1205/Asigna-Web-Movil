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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject
import java.lang.Exception
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
            if (id == "") {
                val parametros = JSONObject()
                parametros.put("nombre_completo", txtNombre_completo.text.toString())
                parametros.put("nombre_espacio", txtNombre_espacio.text.toString())
                parametros.put("fecha_entrada", txtFecha_entrada.text.toString())
                parametros.put("fecha_salida", txtFecha_salida.text.toString())
                parametros.put("hora_entrada", txtHora_entrada.text.toString())
                parametros.put("hora_salida", txtHora_salida.text.toString())

                val request = JsonObjectRequest(
                    Request.Method.POST,
                    config.urlcrearReserva,
                    parametros,
                    { response ->
                        Toast.makeText(
                            context,
                            "Reserva creada",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    { error ->
                        Toast.makeText(
                            context,
                            "Se generó un error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            } else {
                // Implementar lógica para edición si es necesario
            }
        } catch (error: Exception) {
            // Manejo de errores
        }
    }

    fun mostrarCalendario(text: EditText, esFechaEntrada: Boolean) {
        // Obtener la fecha actual
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Crear el DatePickerDialog calendario
        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            // Formatear la fecha seleccionada
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }

            // Validar que la fecha seleccionada no sea anterior a la actual
            if (selectedDate.before(Calendar.getInstance())) {
                Toast.makeText(requireContext(), "No puedes seleccionar una fecha anterior a la actual.", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                text.setText(formattedDate)
            }
        }, year, month, day)

        // Establecer la fecha mínima en el DatePickerDialog para evitar seleccionar fechas pasadas
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    fun mostrarHora(text: EditText, esHoraEntrada: Boolean) {
        // Obtener la hora actual
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Crear el TimePickerDialog
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }

            // Si es hora de entrada, validar que no sea anterior a la hora actual
            if (esHoraEntrada && selectedTime.before(Calendar.getInstance())) {
                Toast.makeText(requireContext(), "No puedes seleccionar una hora anterior a la actual.", Toast.LENGTH_SHORT).show()
            } else {
                // Formatear la hora seleccionada
                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                text.setText(formattedTime)
            }
        }, currentHour, currentMinute, true)

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crearreserva, container, false)

        // Obtener las referencias de los botones y EditText
        val btnCalendario = view.findViewById<ImageView>(R.id.btnCalendario)
        val btnCalendario2 = view.findViewById<ImageView>(R.id.btnCalendario2)
        val txtFechaEntrada = view.findViewById<EditText>(R.id.txtFecha_entrada)
        val txtFechaSalida = view.findViewById<EditText>(R.id.txtFecha_salida)

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
            mostrarCalendario(txtFechaEntrada, true)
        }

        btnCalendario2.setOnClickListener {
            mostrarCalendario(txtFechaSalida, false)
        }

        // Asignar los listeners de clic a los campos de hora
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
