package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject
import java.lang.Exception
import android.util.Patterns

class crear_cuentaa : Fragment() {
    lateinit var txtNumeroDocumento: EditText
    lateinit var txtNombreCompleto: EditText
    lateinit var txtusername: EditText
    lateinit var spinnerTipoDocumento: Spinner
    lateinit var spinnerRolUsuario: Spinner
    private lateinit var btnGuardar: Button
    private var id: String = ""

    private fun mostrarAlertaConRedireccion(titulo: String, mensaje: String, icono: Drawable) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setIcon(icono)
        builder.setPositiveButton("OK") { dialog, which ->
            // Aquí se eliminó la redirección al inicio de sesión
            dialog.dismiss() // Opcional: cerrar la alerta manualmente
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    // Función para mostrar una alerta con un ícono personalizado (sin redirección)
    private fun mostrarAlertaConIcono(titulo: String, mensaje: String, icono: Drawable) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setIcon(icono)
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Función para guardar usuario
    fun guardarUsuario() {
        try {
            // Validar todos los campos
            if (!validarCampos()) {
                return
            }

            if (id == "") {
                val parametros = JSONObject()
                parametros.put("tipo_documento", spinnerTipoDocumento.selectedItem.toString())
                parametros.put("numero_documento", txtNumeroDocumento.text.toString())
                parametros.put("nombre_completo", txtNombreCompleto.text.toString())
                parametros.put("rol", spinnerRolUsuario.selectedItem.toString())
                parametros.put("username", txtusername.text.toString())
                parametros.put("password", "")

                val request = JsonObjectRequest(
                    Request.Method.POST,
                    config.urluserRegistro + "register/",
                    parametros,

                    { response ->
                        // Mostrar la alerta de éxito con redirección al inicio de sesión
                        mostrarAlertaConRedireccion("Éxito", "Se guardó correctamente", resources.getDrawable(android.R.drawable.checkbox_on_background, null))
                    },
                    { error ->
                        mostrarAlertaConIcono("Error", "Se generó un error al guardar el usuario", resources.getDrawable(android.R.drawable.ic_dialog_alert, null))
                    }
                )

                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            } else {
                // Código para editar el usuario si es necesario
            }

        } catch (error: Exception) {
            mostrarAlertaConIcono("Error", "Error: ${error.message}", resources.getDrawable(android.R.drawable.ic_dialog_alert, null))
        }
    }

    // Validación de campos vacíos y formatos
    private fun validarCampos(): Boolean {
        if (txtNumeroDocumento.text.isEmpty() || txtNombreCompleto.text.isEmpty()
            || txtusername.text.isEmpty()) {
            txtNumeroDocumento.error = "Todos los campos deben estar llenos"
            return false
        }

        if (!txtNumeroDocumento.text.toString().matches(Regex("\\d{1,20}"))) {
            txtNumeroDocumento.error = "El número de documento debe tener solo números y máximo 20 caracteres"
            return false
        }

        if (!txtNombreCompleto.text.toString().matches(Regex("^[a-zA-Z]+( [a-zA-Z]+){0,3}$"))) {
            txtNombreCompleto.error = "El nombre completo debe contener solo letras y hasta 3 espacios"
            return false
        }

        val email = txtusername.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtusername.error = "El correo electrónico no es válido"
            return false
        }

        return true
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
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Inicialización de los Spinners
        spinnerTipoDocumento = view.findViewById(R.id.SpinnerTipoDocumento)
        spinnerRolUsuario = view.findViewById(R.id.SpinnerRolUsuario)

        // Configurar el Spinner para Tipo de Documento
        val opciones = arrayOf("Cédula de ciudadanía", "Tarjeta de identidad", "Cédula de extranjería")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = adapter

        // Configurar el Spinner para Rol de Usuario
        val opcionesRol = arrayOf("Usuario", "Administrador")
        val adapterRol = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesRol)
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRolUsuario.adapter = adapterRol

        btnGuardar.setOnClickListener {
            guardarUsuario()
        }

        return view
    }
}
