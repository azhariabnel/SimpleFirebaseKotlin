package com.ari.firebaseloginapp.utils

class TextValidator {
    companion object {
        fun validateName(name: String): Boolean {
            return name.trim().matches(Const.NAME_PATTERN.toRegex())
        }
    }
}