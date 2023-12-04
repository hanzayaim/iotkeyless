package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.iotkeyless.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        isUserLogin()
        goToRegister()
    }

    private fun isUserLogin() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.etEmailForLogin.text.toString()
            val password = binding.etPasswordForLogin.text.toString()

            if (validateInputs(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Empty fields are not allowed!")
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateTo(HomeActivity::class.java)
            }
            else {
                Log.e("LoginActivity", "Login failed", task.exception)
                showToast(task.exception?.toString() ?: "Login failed.")
            }
        }
    }

    private fun goToRegister() {
        binding.tvGoToRegister.setOnClickListener {
            navigateTo(RegisterActivity::class.java)
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