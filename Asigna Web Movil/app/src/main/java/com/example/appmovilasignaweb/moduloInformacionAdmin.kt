package com.example.appmovilasignaweb

import EspacioAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONException

class moduloInformacionAdmin : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var espacioAdapter: EspacioAdapter
    private var espacioList = mutableListOf<Espacio>() // Lista mutable de espacios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espacios)

        // Inicializa el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEspacios)

        // Crea e inicializa el adaptador con la función de clic
        espacioAdapter = EspacioAdapter(this, espacioList) { espacio ->
            // Navegar a Contenedor_crear_reserva y pasar los datos del espacio seleccionado
            val intent = Intent(this, Contenedor_crear_reserva::class.java).apply {
                putExtra("nombre", espacio.nombre)
                putExtra("clasificacion", espacio.clasificacion)
                putExtra("capacidad", espacio.capacidad)
                putExtra("descripcion", espacio.descripcion)
            }
            startActivity(intent)
        }

        // Establece el adaptador
        recyclerView.adapter = espacioAdapter

        // Configura el LayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ajustar padding de la vista para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Llamar a la API
        consultarAPI()
    }

    // Método para consultar la API
    private fun consultarAPI() {
        val url = config.urlEspacios // Cambia esta URL por la de tu API

        // Crear una cola de peticiones
        val queue = Volley.newRequestQueue(this)

        // Crear la petición GET
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    // Procesar el JSONArray directamente
                    espacioList.clear() // Limpiar la lista antes de agregar nuevos datos
                    for (i in 0 until response.length()) {
                        val espacioJson = response.getJSONObject(i)
                        val espacio = Espacio(
                            nombre = espacioJson.getString("nombre_del_espacio"),
                            clasificacion = espacioJson.getString("clasificacion"),
                            capacidad = espacioJson.getString("capacidad"),
                            descripcion = espacioJson.getString("descripcion")
                        )
                        espacioList.add(espacio)
                    }
                    espacioAdapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
                    Toast.makeText(this, "Datos cargados exitosamente!", Toast.LENGTH_SHORT).show()
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

        // Añadir la solicitud a la cola de peticiones
        queue.add(jsonArrayRequest)
    }

    // Métodos de navegación a otras actividades
    fun irreserva(view: View) {
        val intent = Intent(this, Reserva::class.java)
        startActivity(intent)
    }

    fun volver(view: View) {
        val intent = Intent(this, inicio_sesion::class.java)
        startActivity(intent)
    }

    fun irAgregarReserva(view: View) {
        val intent = Intent(this, Contenedor_crear_reserva::class.java)
        startActivity(intent)
    }


    fun irMiPerfil(view: View) {
        val intent = Intent(this, miPerfilAdmin::class.java)
        startActivity(intent)
    }

    fun irmoduloinformacionadministrador(view: View) {
        val intent = Intent(this, moduloInformacionAdmin::class.java)
        startActivity(intent)
    }
}