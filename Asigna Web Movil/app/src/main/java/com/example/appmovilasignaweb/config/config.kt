package com.example.appmovilasignaweb.config

class config {

    companion object {
        val urlBase = "http://10.192.66.189:8080/api/v1/"
        val urluserRegistro = urlBase + "user/"
        val urlcrearReserva = urlBase + "reserva/"

        val urllogin = urlBase + "public/user"
        val urlProfile = urlBase + "user/profile"

        // Agrega la nueva URL para cambiar contraseña
        val urlCambiarContrasena = urlBase + "user/cambiar-contrasena"
        val urlverificarcontrasena = urlBase + "user/verificar-contrasena"

        val urlRecuperarContrasena = urlBase + "user/recuperar-contrasena"

        //cambiar contraseña 2

        val urlCambiarcontra = urlBase + "user/cambiar-contrasena"
    }
}