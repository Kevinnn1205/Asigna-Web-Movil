package com.example.appmovilasignaweb

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

class modificar_datoss : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    // Declaración de las vistas y Spinners
    private lateinit var txtNumeroDocumento: EditText
    private lateinit var txtNombre_completo: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtusername: EditText
    private lateinit var btnGuardar: Button
    private lateinit var spinnerTipoDocumento: Spinner

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
        txtTelefono = view.findViewById(R.id.txtTelefono)
        txtusername = view.findViewById(R.id.txtusername)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicialización de los Spinners
        spinnerTipoDocumento = view.findViewById(R.id.SpinnerTipoDocumento)

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

        // Limitar el número de caracteres del Teléfono y solo permitir números
        txtTelefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString()
                // Limitar a 10 caracteres y permitir solo números
                if (texto.length > 10) {
                    txtTelefono.setText(texto.substring(0, 10))
                    txtTelefono.setSelection(10)
                }
                if (!TextUtils.isDigitsOnly(texto)) {
                    txtTelefono.error = "Solo se permiten números"
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

        // Validación del Teléfono (no vacío y numérico)
        val telefono = txtTelefono.text.toString()
        if (TextUtils.isEmpty(telefono)) {
            txtTelefono.error = "El teléfono es obligatorio"
            esValido = false
        } else if (telefono.length < 10) {
            txtTelefono.error = "El teléfono debe tener al menos 10 dígitos"
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
