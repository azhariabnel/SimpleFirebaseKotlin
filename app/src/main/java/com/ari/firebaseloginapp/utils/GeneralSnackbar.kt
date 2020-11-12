package com.ari.firebaseloginapp.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.ari.firebaseloginapp.R
import com.google.android.material.snackbar.Snackbar

class GeneralSnackbar {
    companion object {
        fun showErrorSnackBar(view: View, msg: String, context: Context) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).apply {
                this.view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                            R.color.colorBrightRed
                    )
                )
            }.show()
        }
        fun successMessage(view : View, msg: String, context: Context){
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).apply {
                this.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
            }.show()
        }
    }
}