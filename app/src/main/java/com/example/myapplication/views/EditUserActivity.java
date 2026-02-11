package com.example.myapplication.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.managers.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class EditUserActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etAltEmail, etPhone;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etName = findViewById(R.id.editTextUserName);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etAltEmail = findViewById(R.id.editTextTextEmailAddress2);
        etPhone = findViewById(R.id.editTextPhone);

        MaterialButton btnSave = findViewById(R.id.buttonRegister); // Reutiliza ID o cámbialo

        currentUser = getIntent().getParcelableExtra("user");
        if (currentUser.isAdmin()) {
            etEmail.setEnabled(false);  // No permite editar correo del admin
        }

        if (currentUser == null) {
            Toast.makeText(this, "Error al cargar usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Precargar
        etName.setText(currentUser.getName());
        etEmail.setText(currentUser.getEmail());
        etAltEmail.setText(currentUser.getAltEmail());
        etPhone.setText(currentUser.getPhone());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String altEmail = etAltEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();


            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Nombre y email son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar
            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setAltEmail(altEmail);
            currentUser.setPhone(phone);

            UserManager.getInstance().updateUser(currentUser);
            Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}