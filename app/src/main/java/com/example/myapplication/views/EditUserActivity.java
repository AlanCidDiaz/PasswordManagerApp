package com.example.myapplication.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.presenters.EditUserPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class EditUserActivity extends AppCompatActivity implements EditUserPresenter.View {

    private TextInputEditText etName, etEmail, etAltEmail, etPhone;
    private User currentUser;
    private EditUserPresenter presenter;

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

        // Crear Presenter y pasarle this como View
        presenter = new EditUserPresenter(this, this, currentUser);

        // Precargar datos
        etName.setText(currentUser.getName());
        etEmail.setText(currentUser.getEmail());
        etAltEmail.setText(currentUser.getAltEmail());
        etPhone.setText(currentUser.getPhone());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String altEmail = etAltEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            presenter.saveChanges(name, email, altEmail, phone);
        });
    }

    // Implementación de la interfaz View del Presenter
    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        setResult(RESULT_OK);
        finish();
    }
}