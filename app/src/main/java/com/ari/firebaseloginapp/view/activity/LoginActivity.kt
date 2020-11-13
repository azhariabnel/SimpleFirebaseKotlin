package com.ari.firebaseloginapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ari.firebaseloginapp.R
import com.ari.firebaseloginapp.utils.GeneralSnackbar
import com.ari.firebaseloginapp.utils.gone
import com.ari.firebaseloginapp.utils.visible
import com.ari.firebaseloginapp.view.fragment.RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var loginEmail: String? = ""
    private var loginPassword: String? = ""
    private var isAllowedButton: Boolean = false
    private var isEmailFilled: Boolean = false
    private var isPasswordFilled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initListener()
        btnLogin.setOnClickListener {
            if (checkForm()) doLogin()
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        tvRegister.setOnClickListener {
            val dialog = RegisterFragment()
            dialog.show(this.supportFragmentManager.beginTransaction(),"register")
        }

    }

    private fun initListener() {
        etUserName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val s = etUserName.text.toString()
                isEmailFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
        etPasswordUser.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val s = etPasswordUser.text.toString()
                isPasswordFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
    }

    private fun updateButton() {
        val enable =
                isEmailFilled  && isPasswordFilled
        btnLogin.isClickable = enable
        if (enable) {
            tv_err_username.gone()
            tv_error_password.gone()
            enableButtonLogin()
        } else {
            disableButtonLogin()
        }
    }

    private fun checkForm() : Boolean {
        loginEmail = etUserName.text.toString()
        loginPassword = etPasswordUser.text.toString()
        if (TextUtils.isEmpty(loginEmail)){
            tv_err_username.text = this.getString(R.string.tidak_boleh_kosong)
            tv_err_username.visible()
            etUserName.requestFocus()
        } else {
            tv_err_username.gone()
        }
        if (TextUtils.isEmpty(loginPassword)){
            tv_error_password.text = this.getString(R.string.password_tidak_boleh_kosong)
            tv_error_password.visible()
            etPasswordUser.requestFocus()
        } else if (!TextUtils.isEmpty(loginPassword) && loginPassword!!.length < 8){
            tv_error_password.text = this.getString(R.string.minimum_password)
            tv_error_password.visible()
            etPasswordUser.requestFocus()
        } else {
            tv_error_password.gone()
        }

        updateIsAllowed()

        return isAllowedButton
    }

    private fun updateIsAllowed() {
        isAllowedButton =
                isEmailFilled && isPasswordFilled
        if (isAllowedButton) enableButtonLogin() else disableButtonLogin()
    }

    private fun disableButtonLogin() {
        btnLogin.isClickable = false
    }

    private fun enableButtonLogin() {
        btnLogin.isClickable = true
    }

    private fun doLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.toString(),loginPassword.toString())
                .addOnCompleteListener {
                    if (!it.isSuccessful) { return@addOnCompleteListener

                    } else {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener {
                    GeneralSnackbar.showErrorSnackBar(rootView,"Email/Password salah",rootView.context)
                }
        Log.d("Login", "Masuk dengan $loginEmail")
    }

}