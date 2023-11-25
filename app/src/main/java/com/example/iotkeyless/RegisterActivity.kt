package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.iotkeyless.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        isUserRegister()
        goToLogin()
    }

    private fun isUserRegister() {
        binding.buttonRegister.setOnClickListener {
            val email = binding.etEmailForRegister.text.toString()
            val password = binding.etPasswordForRegister.text.toString()
            val confirmPassword = binding.etConfirmationPasswordForRegister.text.toString()

            if (validateInputs(email, password, confirmPassword)) {
                registerUser(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Empty fields are not allowed!")
            return false
        }

        if (password != confirmPassword) {
            showToast("Password is not matching!")
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateTo(LoginActivity::class.java)
            }
            else {
                Log.e("RegisterActivity", "Registration failed", task.exception)
                showToast(task.exception?.toString() ?: "Registration failed.")
            }
        }
    }

    private fun goToLogin() {
        binding.tvGoToLogin.setOnClickListener {
            navigateTo(LoginActivity::class.java)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }
}