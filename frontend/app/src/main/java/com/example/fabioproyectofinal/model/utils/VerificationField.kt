package com.example.fabioproyectofinal.model.utils

object VerificationField {

    /* Requisitos de contraseña:
    Al menos 4 letras
    Al menos un número
    Solo puede contener letras, números y los símbolos: - / . / _
    No puede contener espacios
    */

    fun isPasswordValid(password: String): Boolean {
        val letterCount = password.count { it.isLetter() }
        val hasNumber = password.any { it.isDigit() }
        val hasOnlyAllowedChars = password.all { it.isLetterOrDigit() || it in "-._" }
        val hasNoSpaces = !password.contains(" ")
        return letterCount >= 4 && hasNumber && hasOnlyAllowedChars && hasNoSpaces
    }

    /* Requisitos de nombre de usuario:
    Mínimo 6 caracteres y máximo 20
    Solo puede contener letras, números y los símbolos: . /
    No puede contener espacios
    */

    fun isUsernameValid(username: String): Boolean {
        val isCorrectLength = username.length in 6..20
        val hasOnlyAllowedChars = username.all { it.isLetterOrDigit() || it in "." }
        val hasNoSpaces = !username.contains(" ")
        return isCorrectLength && hasOnlyAllowedChars && hasNoSpaces
    }
    fun isEmailValid(email: String): Boolean {
        return email.contains("@") && email.length >= 5
    }
}