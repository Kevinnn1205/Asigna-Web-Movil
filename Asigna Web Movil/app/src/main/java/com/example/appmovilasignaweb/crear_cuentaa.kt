package com.example.appmovilasignaweb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject
import java.lang.Exception
import java.util.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [crear_cuentaa.newInstance] factory method to
 * create an instance of this fragment.
 */
class crear_cuentaa : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var txtTipoDocumento: EditText
    lateinit var txtNumeroDocumento: EditText
    lateinit var txtNombreCompleto: EditText
    lateinit var txtRolUsuario: EditText
    lateinit var txtTelefono: EditText
    lateinit var txtusername: EditText

    private lateinit var btnGuardar: Button

    private var id: String = ""

    // Función para guardar usuario
    fun guardarUsuario(){
        try {
            if (id==""){

                var parametros= JSONObject()
                parametros.put("tipo_documento", txtTipoDocumento.text.toString())
                parametros.put("numero_documento", txtNumeroDocumento.text.toString())
                parametros.put("nombre_completo", txtNombreCompleto.text.toString())
                parametros.put("rol", txtRolUsuario.text.toString())
                parametros.put("telefono", txtTelefono.text.toString())
                parametros.put("username", txtusername.text.toString())

                // Generar el código aleatorio y agregarlo a los parámetros
                var codigoGenerado = codigoAleatorio()
                parametros.put("codigo", codigoGenerado) // Agregar el código aleatorio

                var request= JsonObjectRequest(
                    Request.Method.POST,
                    config.urluserRegistro,
                    parametros,

                    { response ->
                        Toast.makeText(
                            context,
                            "Se guardó correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    { error ->
                        Toast.makeText(
                            context,
                            "Se generó un error",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )

                var queue = Volley.newRequestQueue(context)
                queue.add(request)
            } else {
                // Código para editar el usuario si es necesario
            }

        } catch (error: Exception) {
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Función para generar un código aleatorio
    private fun codigoAleatorio(): String {
        val longitud = 10
        val banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@$%#"
        val cadena = StringBuilder()

        for (x in 0 until longitud) {
            val indiceAleatorio = numeroAleatorioEnRango(0, banco.length - 1)
            val caracterAleatorio = banco[indiceAleatorio]
            cadena.append(caracterAleatorio)
        }

        return cadena.toString()
    }

    // Función auxiliar para generar un número aleatorio en un rango
    private fun numeroAleatorioEnRango(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt((max - min) + 1) + min
    }

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
        val view = inflater.inflate(R.layout.fragment_crear_cuentaa, container, false)
        txtTipoDocumento = view.findViewById(R.id.txtTipoDocumento)
        txtNumeroDocumento = view.findViewById(R.id.txtNumeroDocumento)
        txtNombreCompleto = view.findViewById(R.id.txtNombreCompleto)
        txtRolUsuario = view.findViewById(R.id.txtRolUsuario)
        txtusername = view.findViewById(R.id.txtusername)
        txtTelefono = view.findViewById(R.id.txtTelefono)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            guardarUsuario()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            crear_cuentaa().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
