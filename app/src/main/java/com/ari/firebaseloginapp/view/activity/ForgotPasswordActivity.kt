package com.ari.firebaseloginapp.view.activity

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.ari.firebaseloginapp.R
import com.ari.firebaseloginapp.utils.GeneralSnackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.toast

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        img_arrow_back.setOnClickListener {
            finish()
        }

        btnReset.setOnClickListener {
            val email = etEmailLupa.text.toString()
            if (TextUtils.isEmpty(email)) {
                toast("Email Harus diisi!")
            } else {
                sendForgotEmail(email)
            }
        }
    }

    private fun sendForgotEmail(email: String) {
        val user = FirebaseAuth.getInstance()
        user.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    GeneralSnackbar.successMessage(
                        rootView,
                        "Reset Password link berhasil dikirim",
                        rootView.context
                    )
                } else {
                    GeneralSnackbar.showErrorSnackBar(rootView,"Password Reset email gagal dikirim",rootView.context)
                }
            }
    }
}