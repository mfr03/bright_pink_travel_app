package com.example.uts_ppab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uts_ppab.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar


class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)


        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            loginButton.setOnClickListener {
                if (inputUsername.text.toString().isEmpty()  || inputPassword.text.toString().isEmpty()) {
                    Snackbar.make(
                        binding.root,
                        "Mohon isi bagan yang kosong",
                        Snackbar.LENGTH_SHORT
                    ).setAnchorView(loginButton).show()
                } else {
                    if (inputUsername.text.toString() == intent.getStringExtra("username") && inputPassword.text.toString() == intent.getStringExtra(
                            "password"
                        )
                    ) {
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}