package com.example.appmovilasignaweb

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class espacios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_espacios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        consultarAPI()
    }

    // Método para consultar la API
    private fun consultarAPI() {
        // URL de tu API
        val url = config.urlEspacios// Cambia esta URL por la de tu API

        // Crea una cola de peticiones
        val queue = Volley.newRequestQueue(this)

        // Crea la petición GET
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    // Maneja la respuesta aquí
                    // Por ejemplo, puedes extraer datos del JSON
                    // val espacios = response.getJSONArray("espacios")
                    Toast.makeText(this, "Consulta exitosa!", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error en la respuesta", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error en la solicitud", Toast.LENGTH_SHORT).show()
            }
        )

        // Añade la petición a la cola
        queue.add(jsonObjectRequest)
    }

    // Method to handle the Fragment transaction
    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main, fragment) // Ensure R.id.fragment_container exists in your layout
        transaction.addToBackStack(null) // Optional: allows the user to navigate back
        transaction.commit()
    }

    fun irreserva(view: View) {
        var intent = Intent(application, Reserva::class.java)
        startActivity(intent)
    }
    fun volver(view: View) {
        var intent = Intent(application, inicio_sesion::class.java)
        startActivity(intent)
    }

    fun irAgregarReserva(view: View) {
        var intent = Intent(application, Contenedor_crear_reserva::class.java)
        startActivity(intent)
    }

    fun irModuloInformacion(view: View) {
        var intent = Intent(application, espacios::class.java)
        startActivity(intent)
    }

    fun irmiperfil(view: View) {
        var intent = Intent(application, miperfil::class.java)
        startActivity(intent)
    }
}