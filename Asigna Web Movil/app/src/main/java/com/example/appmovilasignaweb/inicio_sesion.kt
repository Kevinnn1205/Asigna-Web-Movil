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
import com.example.appmovilasignaweb.config.config.Companion.urlBase
import com.example.appmovilasignaweb.config.config.Companion.urllogin
import com.example.appmovilasignaweb.config.config.Companion.urlverificarcontrasena
import org.json.JSONException
import org.json.JSONObject
import android.app.ProgressDialog
import com.example.appmovilasignaweb.config.config

class inicio_sesion : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialog: ProgressDialog // Declarar el ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        // Inicializar ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Por favor, espere...")
            setCancelable(false) // Evita que el usuario lo cierre mientras está activo
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

        // Mostrar el ProgressDialog
        progressDialog.show()

        val url = urllogin + "/login"

        val jsonBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    val token = response.getString("token")

                    // Guardar el token en SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("token", token)
                    editor.apply()

                    // Llamar a la función para verificar el estado del usuario
                    checkUserStatus(token)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                } finally {
                    // Ocultar el ProgressDialog
                    progressDialog.dismiss()
                }
            },
            Response.ErrorListener { error ->
                // Manejar el error
                error.printStackTrace()
                showAlert("Credenciales incorrectas. Inténtalo de nuevo.", "Aceptar")
                // Ocultar el ProgressDialog
                progressDialog.dismiss()
            }
        ) {}

        requestQueue.add(jsonObjectRequest)
    }

    private fun checkUserStatus(token: String) {
        val url = urlBase + "user/verificar-estado"

        // Mostrar el ProgressDialog
        progressDialog.show()

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val estado = response.getString("estado")

                    if (estado != "Activo") {
                        showAlert("La cuenta está desactivada", "Aceptar")
                        return@Listener
                    }

                    // Si la cuenta está activa, proceder a verificar la contraseña
                    verificarEstadoContrasena(token)

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al verificar el estado del usuario", Toast.LENGTH_SHORT).show()
                } finally {
                    // Ocultar el ProgressDialog
                    progressDialog.dismiss()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al verificar el estado del usuario", Toast.LENGTH_SHORT).show()
                // Ocultar el ProgressDialog
                progressDialog.dismiss()
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

    private fun verificarEstadoContrasena(token: String) {
        val url = urlverificarcontrasena

        // Mostrar el ProgressDialog
        progressDialog.show()

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    val verificarContrasena = response.getBoolean("verificar_contrasena")

                    if (verificarContrasena) {
                        val intent = Intent(this, cambiarcontrasena::class.java)
                        startActivity(intent)
                    } else {
                        obtenerRol(token)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error al verificar el estado de la contraseña", Toast.LENGTH_SHORT).show()
                } finally {
                    // Ocultar el ProgressDialog
                    progressDialog.dismiss()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al verificar el estado de la contraseña", Toast.LENGTH_SHORT).show()
                // Ocultar el ProgressDialog
                progressDialog.dismiss()
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


    private fun showAlert(message: String, buttonText: String, onClickAction: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
                onClickAction?.invoke()
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
