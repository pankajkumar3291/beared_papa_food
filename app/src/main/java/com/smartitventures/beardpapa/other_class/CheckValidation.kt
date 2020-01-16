package com.smartitventures.beardpapa.other_class

import android.text.TextUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

class CheckValidation {
    companion object {
        fun isValidPassword(password: String): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
            pattern = Pattern.compile(PASSWORD_PATTERN)
            matcher = pattern.matcher(password)
            return matcher.matches()
        }
        fun phoneIsValidate(number: String): Boolean {
            if (number.length < 10) {
                return false
            } else {
                return true
            }
        }
    }
}