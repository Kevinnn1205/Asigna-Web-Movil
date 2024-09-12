package com.example.appmovilasignaweb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject
import java.lang.Exception
import java.util.Random
import android.text.InputFilter
import android.text.Spanned

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class crear_cuentaa : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var txtNumeroDocumento: EditText
    lateinit var txtNombreCompleto: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtusername: EditText

    lateinit var spinnerTipoDocumento: Spinner
    lateinit var spinnerRolUsuario: Spinner

    private lateinit var btnGuardar: Button

    private var id: String = ""

    // Función para guardar usuario
    fun guardarUsuario() {
        try {
            // Validar todos los campos
            if (!validarCampos()) {
                return
            }

            if (id == "") {
                var parametros = JSONObject()
                parametros.put("tipo_documento", spinnerTipoDocumento.selectedItem.toString()) // Obtiene el valor del Spinner
                parametros.put("numero_documento", txtNumeroDocumento.text.toString())
                parametros.put("nombre_completo", txtNombreCompleto.text.toString())
                parametros.put("rol", spinnerRolUsuario.selectedItem.toString()) // Obtiene el valor del Spinner
                parametros.put("telefono", txtTelefono.text.toString())
                parametros.put("username", txtusername.text.toString())

                // Generar el código aleatorio y agregarlo a los parámetros
                var codigoGenerado = codigoAleatorio()
                parametros.put("codigo", codigoGenerado) // Agregar el código aleatorio

                var request = JsonObjectRequest(
                    Request.Method.POST,
                    config.urluserRegistro,
                    parametros,

                    { response ->
                        Toast.makeText(
                            context,
                            "Se guardó correctamente",
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

                var queue = Volley.newRequestQueue(context)
                queue.add(request)
            } else {
                // Código para editar el usuario si es necesario
            }

        } catch (error: Exception) {
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Función para generar un código aleatorio
    private fun codigoAleatorio(): String {
        val longitud = 10
        val banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@$%#"
        val cadena = StringBuilder()

        for (x in 0 until longitud) {
            val indiceAleatorio = numeroAleatorioEnRango(0, banco.length - 1)
            val caracterAleatorio = banco[indiceAleatorio]
            cadena.append(caracterAleatorio)
        }

        return cadena.toString()
    }

    // Función auxiliar para generar un número aleatorio en un rango
    private fun numeroAleatorioEnRango(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt((max - min) + 1) + min
    }

    // Validación de campos vacíos y formatos
    private fun validarCampos(): Boolean {
        // Validar que los campos no estén vacíos
        if (txtNumeroDocumento.text.isEmpty() || txtNombreCompleto.text.isEmpty() ||
            txtTelefono.text.isEmpty() || txtusername.text.isEmpty()) {
            Toast.makeText(context, "Todos los campos deben estar completos", Toast.LENGTH_LONG).show()
            return false
        }

        // Validación de número de documento (solo números y hasta 20 caracteres)
        if (!txtNumeroDocumento.text.toString().matches(Regex("\\d{1,20}"))) {
            Toast.makeText(context, "El número de documento debe tener solo números y máximo 20 caracteres", Toast.LENGTH_LONG).show()
            return false
        }

        // Validación de nombre completo (solo letras)
        if (!txtNombreCompleto.text.toString().matches(Regex("^[a-zA-Z\\s]+$"))) {
            Toast.makeText(context, "El nombre completo debe contener solo letras", Toast.LENGTH_LONG).show()
            return false
        }

        // Validación de teléfono (solo números y 10 dígitos)
        if (!txtTelefono.text.toString().matches(Regex("\\d{10}"))) {
            Toast.makeText(context, "El teléfono debe tener solo 10 números", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    // Función para limitar el número de caracteres en el campo de número de documento
    private fun setMaxLength(editText: EditText, length: Int) {
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(length))
        editText.filters = filterArray
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crear_cuentaa, container, false)

        // Inicialización de las vistas
        txtNumeroDocumento = view.findViewById(R.id.txtNumeroDocumento)
        txtNombreCompleto = view.findViewById(R.id.txtNombreCompleto)
        txtusername = view.findViewById(R.id.txtusername)
        txtTelefono = view.findViewById(R.id.txtTelefono)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicialización de los Spinners
        spinnerTipoDocumento = view.findViewById(R.id.SpinnerTipoDocumento)
        spinnerRolUsuario = view.findViewById(R.id.SpinnerRolUsuario)

        // Aplicar validaciones de formato
        setMaxLength(txtNumeroDocumento, 20)  // Limitar a 20 caracteres

        btnGuardar.setOnClickListener {
            guardarUsuario()
        }

        // Configurar el Spinner para Tipo de Documento
        val opciones = arrayOf("Cedula", "Tarjeta de identidad", "cedula de extranjeria")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adapter

        // Configurar el Spinner para Rol de Usuario
        val opciones2 = arrayOf("Usuario", "Administrador")
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRolUsuario.adapter = adapter2


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            crear_cuentaa().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
