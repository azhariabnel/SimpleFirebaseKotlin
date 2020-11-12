package com.ari.firebaseloginapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ari.firebaseloginapp.R
import com.ari.firebaseloginapp.model.User
import com.ari.firebaseloginapp.utils.GeneralSnackbar
import com.ari.firebaseloginapp.utils.TextValidator
import com.ari.firebaseloginapp.utils.invisible
import com.ari.firebaseloginapp.utils.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.support.v4.ctx


class RegisterFragment : DialogFragment() {

    private var isAllowedButton: Boolean = false
    private var isNameFilled: Boolean = false
    private var isEmailFilled: Boolean = false
    private var isPasswordEquals: Boolean = false
    private var isEmailPatternValid: Boolean = false
    private var isPasswordLengthMeet: Boolean = false
    private var registerName: String? = ""
    private var registerEmail: String? = ""
    private var registerPassword: String? = ""


    private var isPasswordConfirmFilled: Boolean = false
    private var isPasswordFilled: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_FirebaseLoginApp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        button_next.setOnClickListener {
            if (validateForm()) sendUpdate()
        }
        tv_login_here.setOnClickListener {
            dismiss()
        }

    }

    private fun initListener() {
        etNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val s = etNama.text.toString()
                isNameFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val s = etEmail.text.toString()
                isEmailFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val s = etPassword.text.toString()
                isPasswordFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
        etPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val s = etPasswordConfirm.text.toString()
                isPasswordConfirmFilled = s != null && s.isNotEmpty()
                updateButton()
            }

        })
    }

    private fun updateButton() {
        val enable =
                isEmailFilled && isNameFilled && isPasswordFilled && isPasswordConfirmFilled
        button_next.isClickable = enable
        if (enable) {
            tvErrorName.invisible()
            tvErrorEmail.invisible()
            tvErrorPassword.invisible()
            tvErrorPasswordConfirm.invisible()
            enableButtonRegister()
        } else {
            disableButtonRegister()
        }
    }

    private fun sendUpdate() {
        if (registerEmail.isNullOrEmpty() || registerPassword.isNullOrEmpty()) {
            Toast.makeText(ctx, "Data kosong", Toast.LENGTH_SHORT).show()
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            registerEmail.toString(),
            registerPassword.toString()
        )
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    sendVerificationEmail()
                }
                .addOnFailureListener{
                    GeneralSnackbar.showErrorSnackBar(
                        rootRegistration,
                        "Registrasi Gagal",
                        rootRegistration.context
                    )
                }
        saveUserToDatabase()

    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    GeneralSnackbar.successMessage(
                        rootRegistration,
                        "Registrasi Berhasil, Harap untuk verifikasi melalui email",
                        rootRegistration.context
                    )
                }
            }
    }

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid!!
        val ref = FirebaseDatabase.getInstance().getReference("users")

        val user = User(uid, registerName.toString(), registerEmail.toString())

        ref.push().key
        ref.child(uid).setValue(user).addOnSuccessListener {
            Log.d("Register", "Data berhasil disimpan")
        }
    }

    private fun validateForm(): Boolean {
        registerName = etNama.text.toString()
        registerEmail = etEmail.text.toString()
        registerPassword = etPassword.text.toString()
        var confirmPassword = etPasswordConfirm.text.toString()

        if (registerName.isNullOrEmpty()) {
            tvErrorName.text = ctx.getString(R.string.tidak_boleh_kosong)
            tvErrorName.visible()
            etNama.requestFocus()
            isNameFilled = false
            disableButtonRegister()
        } else if (!registerName.isNullOrEmpty() && !TextValidator.validateName(registerName!!)) {
            tvErrorName.text = ctx.getString(R.string.error_name_validation)
            tvErrorName.visible()
            etNama.requestFocus()
            isNameFilled = false
        } else if (registerName!!.length < 3) {
            tvErrorName.text = ctx.getString(R.string.error_name_validation2)
            tvErrorName.visible()
            etNama.requestFocus()
            isNameFilled = false
        } else if (registerName!!.length > 50) {
            tvErrorName.text = ctx.getString(R.string.error_name_validation3)
            tvErrorName.visible()
            etNama.requestFocus()
            isNameFilled = false
        } else {
            tvErrorName.invisible()
            isNameFilled = true
        }

        if (TextUtils.isEmpty(registerEmail)) {
            tvErrorEmail.text = ctx.getString(R.string.tidak_boleh_kosong_email)
            tvErrorEmail.visible()
            etEmail.requestFocus()
            isEmailFilled = false
        } else {
            tvErrorEmail.invisible()
            isEmailFilled = true
        }


        if (TextUtils.isEmpty(registerPassword)) {
            tvErrorPassword.text = ctx.getString(R.string.password_tidak_boleh_kosong)
            tvErrorPassword.visible()
            etPassword.requestFocus()
            isPasswordEquals = false
            isPasswordLengthMeet = false
        } else if (!TextUtils.isEmpty(registerPassword) && registerPassword!!.length < 8) {
            tvErrorPassword.text = ctx.getString(R.string.minimum_password)
            tvErrorPassword.visible()
            etPassword.requestFocus()
            isPasswordEquals = false
            isPasswordLengthMeet = false
        } else {
            isPasswordLengthMeet = true
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tvErrorPasswordConfirm.text = ctx.getString(R.string.password_tidak_boleh_kosong)
            tvErrorPasswordConfirm.visible()
            etPasswordConfirm.requestFocus()
            isPasswordEquals = false
            isPasswordLengthMeet = false
        } else if (!TextUtils.isEmpty(confirmPassword) && confirmPassword!!.length < 8) {
            tvErrorPasswordConfirm.text = ctx.getString(R.string.minimum_password)
            tvErrorPasswordConfirm.visible()
            etPasswordConfirm.requestFocus()
            isPasswordEquals = false
            isPasswordLengthMeet = false
        }
        if (registerPassword!!.length >= 8 && confirmPassword.length >= 8 && registerPassword.equals(
                confirmPassword
            )
        ) {
            isPasswordLengthMeet = true
            isPasswordEquals = true
        } else {
            tvErrorPasswordConfirm.text = ctx.getString(R.string.masukan_kembali_password)
            tvErrorPasswordConfirm.visible()
            etPasswordConfirm.requestFocus()
            isPasswordLengthMeet = false
            isPasswordEquals = false
        }

        Log.d("Main", "email : $registerEmail, password : $registerPassword")

        updateIsAllowed()

        return isAllowedButton
    }

    private fun updateIsAllowed() {
        isAllowedButton =
                isEmailFilled && isNameFilled && isPasswordLengthMeet && isPasswordEquals
        if (isAllowedButton) enableButtonRegister() else disableButtonRegister()
    }

    private fun enableButtonRegister() {
        button_next.isClickable = true
    }

    private fun disableButtonRegister() {
        button_next.isClickable = false
    }
}