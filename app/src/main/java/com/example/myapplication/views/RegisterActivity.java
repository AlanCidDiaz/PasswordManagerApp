package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.presenters.RegisterPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etAltEmail, etPhone, etPassword, etConfirmPassword;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar campos
        etName = findViewById(R.id.editTextUserName);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etAltEmail = findViewById(R.id.editTextTextEmailAddress2);
        etPhone = findViewById(R.id.editTextPhone);
        etPassword = findViewById(R.id.editTextTextPassword);
        etConfirmPassword = findViewById(R.id.editTextTextPassword2);

        presenter = new RegisterPresenter(this);

        MaterialButton btnRegister = findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String altEmail = etAltEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (presenter.validateAndRegister(name, email, altEmail, phone, password, confirmPassword)) {
                // Registro exitoso → volver a MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Cierra RegisterActivity
            }
        });
    }
}