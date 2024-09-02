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
import com.example.bibliomayaya.config.config
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Crearreserva.newInstance] factory method to
 * create an instance of this fragment.
 */
class Crearreserva : Fragment() {
    // TODO: Rename and change types of parameters
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


    fun crearReserva(){
        try {
            if (id==""){

                var parametros= JSONObject()
                parametros.put("nombre_completo",txtNombre_completo.text.toString())
                parametros.put("nombre_espacio",txtNombre_espacio.text.toString())
                parametros.put("fecha_entrada",txtFecha_entrada.text.toString())
                parametros.put("fecha_salida",txtFecha_salida.text.toString())
                parametros.put("hora_entrada",txtHora_entrada.text.toString())
                parametros.put("hora_salida",txtHora_salida.text.toString())

                var request= JsonObjectRequest(
                    Request.Method.POST,
                    config.urlcrearReserva,
                    parametros,

                    {response->
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
                            Toast.LENGTH_LONG)
                            .show()


                    }
                )
                //se crea la cola de trabajo y se añade la petición
                var queue= Volley.newRequestQueue(context)
                //se añade la petición
                queue.add(request)
            } else{

            }

        } catch (error: Exception){

        }

    }


    fun mostrarCalendario(text: EditText){
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            text.setText(formattedDate)
        }, year, month, day)

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    // Función para mostrar un TimePickerDialog y seleccionar la hora
    fun mostrarHora(text: EditText) {
        // Obtener la hora actual
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Crear el TimePickerDialog
        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Formatear la hora seleccionada
            val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            text.setText(formattedTime)
        }, hour, minute, true)

        // Mostrar el TimePickerDialog
        timePickerDialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_crearreserva, container, false)

        // Obtener las referencias de los botones y EditText
        val btnCalendario = view.findViewById<ImageView>(R.id.btnCalendario)
        val btnCalendario2 = view.findViewById<ImageView>(R.id.btnCalendario2)
        val txtFechaEntrada = view.findViewById<EditText>(R.id.txtFecha_entrada)
        val txtFechaSalida = view.findViewById<EditText>(R.id.txtFecha_salida)

        // Nuevas referencias para los campos de hora
        txtHora_entrada = view.findViewById(R.id.txtHora_entrada)
        txtHora_salida = view.findViewById(R.id.txtHora_salida)

        // Asignar los listeners de clic a los botones
        btnCalendario.setOnClickListener {
            mostrarCalendario(txtFechaEntrada)
        }

        btnCalendario2.setOnClickListener {
            mostrarCalendario(txtFechaSalida)
        }

        // Asignar los listeners de clic a los campos de hora
        txtHora_entrada.setOnClickListener {
            mostrarHora(txtHora_entrada)
        }

        txtHora_salida.setOnClickListener {
            mostrarHora(txtHora_salida)
        }
        val opciones = arrayOf("Seleccionar...", "Lector", "Biblioteca", "Administrador")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        (txtNombre_espacio as Spinner).adapter = adapter

        btnGuardar.setOnClickListener {
            crearReserva()
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Crearreserva.
         */
        // TODO: Rename and change types and number of parameters
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
