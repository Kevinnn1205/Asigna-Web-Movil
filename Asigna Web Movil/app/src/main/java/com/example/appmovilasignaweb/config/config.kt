package com.example.appmovilasignaweb.config

class config {

    //se crea una url static, para consultar sin instanciar
    //método companion object sirve para almacenar las variables static

    companion object{
        val urlBase="http://192.168.137.1:8080/api/v1/"
        val urluserRegistro=urlBase+"user/"
        val urlcrearReserva=urlBase+"reserva/"

        val urllogin= urlBase+"/public/user/"
    }
}