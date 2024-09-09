package com.example.appmovilasignaweb

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import junit.framework.TestCase.assertEquals
import org.json.JSONObject
import org.junit.Before
import org.junit.Test




class crear_cuentaaTest {

    private lateinit var fragment: crear_cuentaa
    private lateinit var context: Context
    private lateinit var requestQueue: RequestQueue

    @Before
    fun setUp() {
        // Inicia mocks y contexto de prueba
        MockitoAnnotations.openMocks(this)
        context = ApplicationProvider.getApplicationContext()
        fragment = crear_cuentaa()

        // Mocks para los campos de texto
        fragment.txtTipoDocumento = mock(EditText::class.java)
        fragment.txtNumeroDocumento = mock(EditText::class.java)
        fragment.txtNombreCompleto = mock(EditText::class.java)
        fragment.txtRolUsuario = mock(EditText::class.java)
        fragment.txtusername = mock(EditText::class.java)
        fragment.txtTelefono = mock(EditText::class.java)

        // Configura el RequestQueue mock
        requestQueue = mock(RequestQueue::class.java)
        `when`(Volley.newRequestQueue(context)).thenReturn(requestQueue)
    }

    @Test
    fun testGuardarUsuario_exitoso() {

        `when`(fragment.txtTipoDocumento.text.toString()).thenReturn("CC")
        `when`(fragment.txtNumeroDocumento.text.toString()).thenReturn("123456789")
        `when`(fragment.txtNombreCompleto.text.toString()).thenReturn("Juan Pérez")
        `when`(fragment.txtRolUsuario.text.toString()).thenReturn("Usuario")
        `when`(fragment.txtusername.text.toString()).thenReturn("juanperez@mail.com")
        `when`(fragment.txtTelefono.text.toString()).thenReturn("123456789")

        // Llama al método a probar
        fragment.guardarUsuario()

        // Capturar la solicitud que se debería enviar
        val captor = ArgumentCaptor.forClass(JsonObjectRequest::class.java)
        verify(requestQueue).add(captor.capture())

        // Verificar los parámetros de la solicitud
        val jsonObject = captor.value.jsonObject
        assertEquals("CC", jsonObject.getString("tipo_documento"))
        assertEquals("123456789", jsonObject.getString("numero_documento"))
        assertEquals("Juan Pérez", jsonObject.getString("nombre_completo"))
        assertEquals("Usuario", jsonObject.getString("rol"))
        assertEquals("juanperez@mail.com", jsonObject.getString("correo"))
        assertEquals("123456789", jsonObject.getString("telefono"))


        captor.value.listener.onResponse(JSONObject())

        Toast.makeText(context, "Se guardó correctamente", Toast.LENGTH_LONG).show()
    }

    @Test
    fun testGuardarUsuario_falla() {
        // Simular valores vacíos de los EditText
        `when`(fragment.txtTipoDocumento.text.toString()).thenReturn("")
        `when`(fragment.txtNumeroDocumento.text.toString()).thenReturn("")
        `when`(fragment.txtNombreCompleto.text.toString()).thenReturn("")
        `when`(fragment.txtRolUsuario.text.toString()).thenReturn("")
        `when`(fragment.txtusername.text.toString()).thenReturn("")
        `when`(fragment.txtTelefono.text.toString()).thenReturn("")

        fragment.guardarUsuario()

        val captor = ArgumentCaptor.forClass(JsonObjectRequest::class.java)
        verify(requestQueue).add(captor.capture())


        captor.value.errorListener.onErrorResponse(null)

        Toast.makeText(context, "Se generó un error", Toast.LENGTH_LONG).show()
    }
}
