package com.example.appmovilasignaweb

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import com.example.appmovilasignaweb.config.config.Companion.urlBase
import com.example.appmovilasignaweb.config.config.Companion.urllogin
import com.example.appmovilasignaweb.config.config.Companion.urlverificarcontrasena
import org.json.JSONException
import org.json.JSONObject

class inicio_sesion : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MiAppPreferences", MODE_PRIVATE)

        // Inicializar EditTexts y Button
        usernameEditText = findViewById(R.id.txtCorreoelectronico)
        passwordEditText = findViewById(R.id.txtContraseña)
        loginButton = findViewById(R.id.btnGuardar)

        // Configurar el listener del botón de inicio de sesión
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val url = urllogin + "/login/"

        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    val token = response.getString("token")

                    // Mostrar alerta de inicio de sesión exitoso
                    showAlert("Inicio de sesión exitoso", "Aceptar") {
                        // Guardar el token en SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("TOKEN", token)
                        editor.apply()

                        // Verificar el estado de la contraseña
                        verificarEstadoContrasena(token)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error de usuario no encontrado
                error.printStackTrace()
                showAlert("Usuario no encontrado", "Aceptar")
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }

    private fun verificarEstadoContrasena(token: String) {
        val url = urlverificarcontrasena

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val verificarContrasena = response.getBoolean("verificar_contrasena")

                    if (verificarContrasena) {
                        // Si el usuario debe cambiar su contraseña, lo redirigimos a la pantalla de cambio de contraseña
                        val intent = Intent(this, cambiarcontrasena::class.java)
                        startActivity(intent)
                    } else {
                        // Si no debe cambiar la contraseña, ahora verificamos el rol
                        obtenerRol(token)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al verificar el estado de la contraseña", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error
                error.printStackTrace()
                Toast.makeText(this, "Error al verificar el estado de la contraseña", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token" // Si estás usando JWT
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun obtenerRol(token: String) {
        val url = config.urlRol

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val rol = response.getString("role")

                    // Redirigir según el rol del usuario
                    if (rol == "Administrador") {
                        // Si el usuario es administrador
                        val intent = Intent(this, moduloInformacionAdmin::class.java)
                        startActivity(intent)
                    } else if (rol == "Usuario") {
                        // Si es un usuario normal
                        val intent = Intent(this, espacios::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al obtener el rol", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error
                error.printStackTrace()
                Toast.makeText(this, "Error al obtener el rol", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    // Función para mostrar alerta
    private fun showAlert(message: String, buttonText: String, onClickAction: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
                onClickAction?.invoke()  // Ejecutar la acción adicional si está definida
            }
        val alert = builder.create()
        alert.show()
    }

    fun volver(view: View) {
        val intent = Intent(application, MainActivity::class.java)
        startActivity(intent)
    }

    fun irrecuperarcontra(view: View) {
        val intent = Intent(application, recuperar_contra::class.java)
        startActivity(intent)
    }

    fun ircambiarcontrasena(view: View) {
        val intent = Intent(application, cambiarcontrasena::class.java)
        startActivity(intent)
    }
}
