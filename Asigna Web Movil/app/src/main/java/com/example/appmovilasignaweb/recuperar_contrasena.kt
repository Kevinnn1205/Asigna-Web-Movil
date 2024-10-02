package com.example.appmovilasignaweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.example.appmovilasignaweb.config.config // Importa la clase config

class recuperar_contrasena : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var recoverPasswordButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recuperar_contrasena, container, false)

        emailInput = view.findViewById(R.id.emailInput) // Asegúrate de que el ID sea correcto en el layout del fragmento
        recoverPasswordButton = view.findViewById(R.id.btnGuardar) // Asegúrate de que el ID sea correcto en el layout del fragmento

        recoverPasswordButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                RecuperarContrasena(email)
            } else {
                Toast.makeText(activity, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun RecuperarContrasena(email: String) {
        // Usar la URL desde config
        val url = config.urlRecuperarContrasena // Utilizamos la URL de config

        val params = JSONObject()
        params.put("username", email) // Cambiado a "username" para que coincida con el backend

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            url,
            params,
            Response.Listener { response ->
                val message = response.optString("message", "Error inesperado.")
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(activity, "Error al enviar el correo: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                val token = obtenerToken() // Método para obtener el token almacenado
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }

        val queue = Volley.newRequestQueue(activity)
        queue.add(jsonObjectRequest)
    }

    private fun obtenerToken(): String {
        // Aquí debes implementar la lógica para obtener tu token de almacenamiento
        val sharedPreferences = activity?.getSharedPreferences("MiAppPreferences", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences?.getString("TOKEN", "") ?: "" // Retorna el token guardado
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            recuperar_contrasena().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
