package br.com.loadti.todolist.utils

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


private val locate = Locale("pt", "BR")
fun Date.format(): String {

    return SimpleDateFormat("dd/MM/yyyy", locate).format(this)
}

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }
