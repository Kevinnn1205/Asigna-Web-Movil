package com.example.appmovilasignaweb

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crearreserva, container, false)

        // Inicializar las referencias de los componentes de la interfaz
        txtNombre_completo = view.findViewById(R.id.txtNombre_completo)
        txtusername = view.findViewById(R.id.txtusername)
        txtFecha_entrada = view.findViewById(R.id.txtFecha_entrada)
        txtFecha_salida = view.findViewById(R.id.txtFecha_salida)
        txtHora_entrada = view.findViewById(R.id.txtHora_entrada)
        txtHora_salida = view.findViewById(R.id.txtHora_salida)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicializar SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("MiAppPreferences", 0)

        // Inicializar los botones de los calendarios
        val btnCalendario = view.findViewById<ImageView>(R.id.btnCalendario)
        val btnCalendario2 = view.findViewById<ImageView>(R.id.btnCalendario2)

        // Spinner para los espacios
        txtNombre_espacio = view.findViewById(R.id.SpinnerNombreEspacio)

        // Cargar el perfil del usuario
        cargarPerfilUsuario()

        // Cargar los espacios disponibles desde el backend
        cargarEspacios()

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

    private fun cargarPerfilUsuario() {
        val requestQueue = Volley.newRequestQueue(requireContext())
        val urlProfile = config.urlProfile  // URL del endpoint para obtener los datos del perfil del usuario
        val authToken = sharedPreferences.getString("TOKEN", "")  // Obtener el token de SharedPreferences

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            urlProfile,
            null,
            { response ->
                // Establecer los valores en los campos correspondientes
                txtNombre_completo.setText(response.getString("nombre_completo"))
                txtusername.setText(response.getString("username"))
            },
            { error ->
                val networkResponse = error.networkResponse
                if (networkResponse != null && networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Error del servidor al cargar el perfil. Código 500", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al cargar los datos del usuario: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $authToken")
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun cargarEspacios() {
        // Realizar una solicitud GET para obtener los espacios registrados desde el backend
        val requestQueue = Volley.newRequestQueue(requireContext())
        val urlEspacios = config.urlEspacios  // URL de tu API para obtener los espacios
        val authToken = sharedPreferences.getString("TOKEN", "")  // Obtener el token de SharedPreferences

        val jsonArrayRequest = object : JsonObjectRequest(
            Request.Method.GET, urlEspacios, null,
            { response ->
                val espacios = ArrayList<String>()
                for (i in 0 until response.length()) {
                    val espacio = response.getJSONObject(i.toString()).getString("nombre_del_espacio")
                    espacios.add(espacio)
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, espacios)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                txtNombre_espacio.adapter = adapter
            },
            { error ->
                val networkResponse = error.networkResponse
                if (networkResponse != null && networkResponse.statusCode == 500) {
                    Toast.makeText(context, "Error del servidor al cargar espacios. Código 500", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Error al cargar los espacios: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Authorization" to "Bearer $authToken")
            }
        }

        requestQueue.add(jsonArrayRequest)
    }

    private fun crearReserva() {
        try {
            // Verifica que los campos no estén vacíos
            if (txtNombre_completo.text.isEmpty() ||
                txtusername.text.isEmpty() ||
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
            val reservaData = JSONObject().apply {
                put("userRegistro", JSONObject().apply {
                    put("nombre_completo", txtNombre_completo.text.toString())
                    put("username", txtusername.text.toString())
                })
                put("espacio", JSONObject().apply {
                    put("nombre_del_espacio", txtNombre_espacio.selectedItem.toString())
                })
                put("fecha_entrada", txtFecha_entrada.text.toString())
                put("fecha_salida", txtFecha_salida.text.toString())
                put("hora_entrada", txtHora_entrada.text.toString())
                put("hora_salida", txtHora_salida.text.toString())
            }

            // Realizar la solicitud POST al backend
            val authToken = sharedPreferences.getString("TOKEN", "")  // Obtener el token de SharedPreferences

            val request = object : JsonObjectRequest(
                Request.Method.POST,
                config.urlcrearReserva,
                reservaData,
                { response ->
                    Toast.makeText(context, "Reserva creada con éxito", Toast.LENGTH_LONG).show()
                },
                { error ->
                    val errorMessage = error.message ?: "Error desconocido"
                    Toast.makeText(context, "Error al crear la reserva: $errorMessage", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return mutableMapOf("Authorization" to "Bearer $authToken")
                }
            }

            // Agregar la solicitud a la cola de peticiones
            Volley.newRequestQueue(context).add(request)

        } catch (error: Exception) {
            error.printStackTrace()
            Toast.makeText(context, "Error inesperado: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarCalendario(text: EditText, esFechaEntrada: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            text.setText(sdf.format(selectedDate.time))

            if (esFechaEntrada) {
                fechaEntrada = selectedDate
            } else {
                fechaSalida = selectedDate
            }
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun mostrarHora(text: EditText, esHoraEntrada: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            text.setText(sdf.format(selectedTime.time))

            if (esHoraEntrada) {
                horaEntrada = selectedTime
            } else {
                horaSalida = selectedTime
            }
        }, hour, minute, true)

        timePickerDialog.show()
    }
}
