package com.example.appmovilasignaweb

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject

class agregar_espacios : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_espacios)

        // Manejo de insets para bordes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los EditText del layout
        val nombreEspacioEditText = findViewById<EditText>(R.id.txtNombre_nuevo_espacio)
        val clasificacionEspacioEditText = findViewById<EditText>(R.id.txtClasificacion_nuevo_espacio)
        val capacidadEspacioEditText = findViewById<EditText>(R.id.txtCapacidad_nuevo_espacio)
        val descripcionEspacioEditText = findViewById<EditText>(R.id.txtDescripcion_nuevo_espacio)

        // Botón de enviar
        val enviarButton = findViewById<Button>(R.id.btnGuardar)

        enviarButton.setOnClickListener {
            // Obtener los datos ingresados por el usuario
            val nombreEspacio = nombreEspacioEditText.text.toString().trim()
            val clasificacionEspacio = clasificacionEspacioEditText.text.toString().trim()
            val capacidadEspacio = capacidadEspacioEditText.text.toString().trim()
            val descripcionEspacio = descripcionEspacioEditText.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (nombreEspacio.isEmpty() || clasificacionEspacio.isEmpty() || capacidadEspacio.isEmpty() || descripcionEspacio.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar la función para enviar los datos al backend
                agregarNuevoEspacio(nombreEspacio, clasificacionEspacio, capacidadEspacio, descripcionEspacio)
            }
        }
    }

    private fun agregarNuevoEspacio(nombre: String, clasificacion: String, capacidad: String, descripcion: String) {
        // URL de tu API en el backend (modifica esta URL con la correcta)
        val url = config.urlEspacios

        // Crear una solicitud POST con Volley
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                // Manejar la respuesta del servidor
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")

                if (success) {
                    Toast.makeText(this, "Espacio agregado exitosamente.", Toast.LENGTH_SHORT).show()
                    // Aquí podrías limpiar los campos o redirigir a otra pantalla si lo deseas
                } else {
                    Toast.makeText(this, "Hubo un error al agregar el espacio.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Manejar el error
                Toast.makeText(this, "Error de conexión: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {

            // Enviar los parámetros al servidor en el cuerpo de la solicitud
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nombre"] = nombre
                params["clasificacion"] = clasificacion
                params["capacidad"] = capacidad
                params["descripcion"] = descripcion
                return params
            }
        }

        // Agregar la solicitud a la cola de Volley
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}
