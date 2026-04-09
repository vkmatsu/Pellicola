package com.pellicola.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pellicola.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val senha = binding.etSenha.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.etEmail.error = "Digite seu e-mail"
                    binding.etEmail.requestFocus()
                }
                senha.isEmpty() -> {
                    binding.etSenha.error = "Digite sua senha"
                    binding.etSenha.requestFocus()
                }
                else -> {
                    // Sem lógica real — qualquer e-mail/senha entra
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        binding.tvEsqueceuSenha.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em breve!", Toast.LENGTH_SHORT).show()
        }
    }
}
