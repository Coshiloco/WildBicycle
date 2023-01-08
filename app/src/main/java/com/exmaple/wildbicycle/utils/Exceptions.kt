package com.exmaple.wildbicycle.utils

class UserNotFoundException(message: String = "Usuario no encontrado en la BBDD") : Throwable(message)
class UserEmailNotIntroducingException(message: String = "Email esta vacio") : Throwable(message)
class UserNotFoundEmailException(message: String = "No se encontro el usuario con este correo") : Throwable(message)
class UserErrorLoginException(message: String = "No hay ningun usuario logeado") : Throwable(message)
