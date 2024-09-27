package com.example.appmovilasignaweb

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import com.example.appmovilasignaweb.models.userRegistro
import com.google.gson.Gson

class modificar_datoss : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    // Declaración de las vistas y Spinners
    private lateinit var txtNumeroDocumento: EditText
    private lateinit var txtNombre_completo: EditText
    private lateinit var txtusername: EditText
    private lateinit var btnGuardar: Button
    private lateinit var spinnerTipoDocumento: Spinner
    var id_user: String = ""

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
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_modificar_datoss, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización de las vistas
        txtNumeroDocumento = view.findViewById(R.id.txtNumeroDocumento)
        txtNombre_completo = view.findViewById(R.id.txtNombre_completo)
        txtusername = view.findViewById(R.id.txtusername)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicialización de los Spinners
        spinnerTipoDocumento = view.findViewById(R.id.SpinnerTipoDocumento)
        mostrarPerfil()

        // Configurar el Spinner para Tipo de Documento
        val opciones = arrayOf("Seleccionar", "Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adapter

        // Limitar el número de caracteres del Número de Documento y solo permitir números
        txtNumeroDocumento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString()
                // Limitar a 20 caracteres y permitir solo números
                if (texto.length > 20) {
                    txtNumeroDocumento.setText(texto.substring(0, 20))
                    txtNumeroDocumento.setSelection(20) // Colocar el cursor al final
                }
                if (!TextUtils.isDigitsOnly(texto)) {
                    txtNumeroDocumento.error = "Solo se permiten números"
                }
            }
        })

        // Limitar el número de caracteres del Username (correo) y validar formato
        txtusername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
                    txtusername.error = "Debes ingresar un correo electrónico válido"
                }
            }
        })

        // Acción para el botón guardar
        btnGuardar.setOnClickListener {
            if (validarFormulario()) {
                guardarUsuario()
            }
        }
    }

    fun mostrarPerfil(){
        val sharedPreferences = requireActivity().getSharedPreferences("MiAppPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        val headers = HashMap<String, String>()
        headers["Authorization"] = "Bearer $token"

        val request = object : JsonObjectRequest(
            Request.Method.GET,
            config.urlProfile,
            null,
            Response.Listener { response ->
                val gson = Gson()
                val userRegistro: userRegistro = gson.fromJson(response.toString(), userRegistro::class.java)

                // Buscar la posición del tipo de documento en el Spinner
                val tipoDocumentoPosition = (spinnerTipoDocumento.adapter as ArrayAdapter<String>).getPosition(userRegistro.tipo_documento)
                spinnerTipoDocumento.setSelection(tipoDocumentoPosition)

                txtNumeroDocumento.setText(userRegistro.numero_documento)
                txtNombre_completo.setText(userRegistro.nombre_completo)
                txtusername.setText(userRegistro.username)
                id_user = userRegistro.id_user

            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    context,
                    "Error al consultar",
                    Toast.LENGTH_LONG
                ).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }

        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    // Función para validar el formulario
    private fun validarFormulario(): Boolean {
        var esValido = true

        // Validación del Número de Documento (no vacío y numérico)
        if (TextUtils.isEmpty(txtNumeroDocumento.text.toString())) {
            txtNumeroDocumento.error = "El número de documento es obligatorio"
            esValido = false
        }

        // Validación del Nombre Completo (no vacío y al menos dos palabras)
        val nombreCompleto = txtNombre_completo.text.toString().trim()
        if (TextUtils.isEmpty(nombreCompleto)) {
            txtNombre_completo.error = "El nombre completo es obligatorio"
            esValido = false
        } else if (nombreCompleto.split(" ").size < 2) {
            txtNombre_completo.error = "Debes ingresar al menos dos nombres"
            esValido = false
        }

        // Validación del Username (correo)
        val email = txtusername.text.toString().trim()
        if (TextUtils.isEmpty(email)) {
            txtusername.error = "El correo es obligatorio"
            esValido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtusername.error = "Debes ingresar un correo electrónico válido"
            esValido = false
        }

        // Validación del Spinner (no debe ser la primera opción)
        if (spinnerTipoDocumento.selectedItem == "Seleccionar") {
            Toast.makeText(requireContext(), "Debes seleccionar un tipo de documento", Toast.LENGTH_SHORT).show()
            esValido = false
        }

        return esValido
    }

    // Función que simula guardar el usuario
    private fun guardarUsuario() {
        // Lógica para guardar los datos del usuario
        Toast.makeText(requireContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            modificar_datoss().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
