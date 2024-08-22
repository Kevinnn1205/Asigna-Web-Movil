package com.example.appmovilasignaweb.models

data class userRegistro(
    var id_user: String,
    var tipo_documento:String,
    var numero_documento:String,
    var nombre_completo:String,
    var telefono:String,
    var correo: String,
    var rol: String
)

