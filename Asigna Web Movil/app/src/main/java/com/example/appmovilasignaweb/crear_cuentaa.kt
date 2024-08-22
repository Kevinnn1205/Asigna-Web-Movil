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
import com.example.bibliomayaya.config.config
import org.json.JSONObject
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [crear_cuentaa.newInstance] factory method to
 * create an instance of this fragment.
 */
class crear_cuentaa : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var txtTipoDocumento: EditText
    private lateinit var txtNumeroDocumento: EditText
    private lateinit var txtNombreCompleto: EditText
    private lateinit var txtRol: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCorreo: EditText

    private lateinit var btnGuardar: Button

    private var id: String = ""

    fun guardarUsuario(){
        try {
            if (id==""){

                var parametros= JSONObject()
                parametros.put("tipo_documento",txtTipoDocumento.text.toString())
                parametros.put("numero_documento",txtNumeroDocumento.text.toString())
                parametros.put("nombre_completo",txtNombreCompleto.text.toString())
                parametros.put("rol",txtRol.text.toString())
                parametros.put("telefono",txtTelefono.text.toString())
                parametros.put("correo",txtCorreo.text.toString())

                var request= JsonObjectRequest(
                    Request.Method.POST,
                    config.urluserRegistro,
                    parametros,

                    {response->
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
                            Toast.LENGTH_LONG)
                            .show()


                    }
                )
                //se crea la cola de trabajo y se añade la petición
                var queue= Volley.newRequestQueue(context)
                //se añade la petición
                queue.add(request)
            } else{

            }

        } catch (error: Exception){

        }

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
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_crear_cuentaa, container, false)
        txtTipoDocumento=view.findViewById(R.id.txtTipoDocumento)
        txtNumeroDocumento=view.findViewById(R.id.txtNumeroDocumento)
        txtNombreCompleto=view.findViewById(R.id.txtNombreCompleto)
        txtRol=view.findViewById(R.id.txtRol)
        txtCorreo=view.findViewById(R.id.txtCorreo)
        txtTelefono=view.findViewById(R.id.txtTelefono)
        btnGuardar=view.findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener{guardarUsuario()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment crear_cuentaa.
         */
        // TODO: Rename and change types and number of parameters
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