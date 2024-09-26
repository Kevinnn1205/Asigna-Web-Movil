package com.example.appmovilasignaweb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

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
                enviarSolicitudRecuperacion(email)
            } else {
                Toast.makeText(activity, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun enviarSolicitudRecuperacion(email: String) {
        val url = "http://localhost:8080/api/v1/"

        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener<String> { response ->
                Toast.makeText(activity, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error: VolleyError ->
                Toast.makeText(activity, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["email"] = email
                return params
            }
        }

        val queue = Volley.newRequestQueue(activity)
        queue.add(stringRequest)
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
