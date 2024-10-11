package com.example.appmovilasignaweb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appmovilasignaweb.config.config
import org.json.JSONObject

class agregar_espacio : Fragment() {

    private val PICK_IMAGE = 100
    private lateinit var imageViewPerfil: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_agregar_espacio, container, false)

        // Manejo de insets para bordes
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura el ImageView y el botón para seleccionar la foto
        imageViewPerfil = view.findViewById(R.id.imageView11)
        val btnCamara = view.findViewById<ImageView>(R.id.imageView12)
        btnCamara.setOnClickListener {
            selectImage()
        }

        // Cargar la imagen guardada al iniciar el fragmento
        loadImageFromInternalStorage()

        // Referencias a los EditText del layout
        val nombreEspacioEditText = view.findViewById<EditText>(R.id.txtNombre_nuevo_espacio)
        val clasificacionEspacioEditText = view.findViewById<EditText>(R.id.txtClasificacion_nuevo_espacio)
        val capacidadEspacioEditText = view.findViewById<EditText>(R.id.txtCapacidad_nuevo_espacio)
        val descripcionEspacioEditText = view.findViewById<EditText>(R.id.txtDescripcion_nuevo_espacio)

        // Botón de enviar
        val enviarButton = view.findViewById<Button>(R.id.btnGuardar)
        enviarButton.setOnClickListener {
            // Obtener los datos ingresados por el usuario
            val nombreEspacio = nombreEspacioEditText.text.toString().trim()
            val clasificacionEspacio = clasificacionEspacioEditText.text.toString().trim()
            val capacidadEspacio = capacidadEspacioEditText.text.toString().trim()
            val descripcionEspacio = descripcionEspacioEditText.text.toString().trim()

            // Validar que los campos no estén vacíos
            if (nombreEspacio.isEmpty() || clasificacionEspacio.isEmpty() || capacidadEspacio.isEmpty() || descripcionEspacio.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar la función para enviar los datos al backend
                agregarNuevoEspacio(nombreEspacio, clasificacionEspacio, capacidadEspacio, descripcionEspacio)
            }
        }

        return view
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imageViewPerfil.setImageURI(imageUri)

            // Guardar la imagen en almacenamiento interno
            imageUri?.let { saveImageToInternalStorage(it) }
        } else {
            Toast.makeText(requireContext(), "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri) {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val filename = "profile_image.png"
            val outputStream = requireActivity().openFileOutput(filename, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            Toast.makeText(requireContext(), "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImageFromInternalStorage() {
        try {
            val filename = "profile_image.png"
            val fileInputStream = requireActivity().openFileInput(filename)
            val bitmap = BitmapFactory.decodeStream(fileInputStream)
            imageViewPerfil.setImageBitmap(bitmap)
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "No se encontró la imagen guardada", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(requireContext(), "Espacio agregado exitosamente.", Toast.LENGTH_SHORT).show()
                    // Aquí podrías limpiar los campos o redirigir a otra pantalla si lo deseas
                } else {
                    Toast.makeText(requireContext(), "Hubo un error al agregar el espacio.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Manejar el error
                Toast.makeText(requireContext(), "Error de conexión: ${error.message}", Toast.LENGTH_SHORT).show()
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
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }
}
