package com.example.appmovilasignaweb.config

class config {
    companion object {
        val urlBase = "http://10.192.80.114:8888/api/v1/"
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

        // verificar rol

        val urlRol = urlBase + "user/rol"

        // desactivar cuenta
        // URL para desactivar cuenta (nota el `id_user` como parámetro dinámico)


        val urlDesactivarCuenta = urlBase + "user/desactivar"

        //Traer los espacios
        val urlEspacios=urlBase+"espacio/"
    }
}