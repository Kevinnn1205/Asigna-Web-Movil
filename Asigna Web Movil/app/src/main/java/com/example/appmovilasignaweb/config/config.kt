package com.example.appmovilasignaweb.config

class config {

    //se crea una url static, para consultar sin instanciar
    //método companion object sirve para almacenar las variables static

    companion object{
        val urlBase="http://10.192.80.35:8080/api/v1/"
        val urluserRegistro=urlBase+"user/"
        val urlcrearReserva=urlBase+"reserva/"

        val urllogin= urlBase+"/public/user/"
    }
}