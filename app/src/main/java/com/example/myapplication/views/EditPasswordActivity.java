package com.example.myapplication.views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.presenters.EditPasswordPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class EditPasswordActivity extends AppCompatActivity implements EditPasswordPresenter.View {

    private TextInputEditText etAppName, etUsername, etPassword;
    private Spinner spinnerCategory;
    private EditPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        etAppName = findViewById(R.id.etAppName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        MaterialButton btnSave = findViewById(R.id.btnSavePassword);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        PasswordEntry currentEntry = getIntent().getParcelableExtra("password_entry");
        String ownerEmail = getIntent().getStringExtra("owner_email");
        int realIndex = getIntent().getIntExtra("real_index", -1);

        if (currentEntry == null || ownerEmail == null || realIndex == -1) {
            Toast.makeText(this, "Error al cargar cuenta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Crear Presenter y pasarle this como View
        presenter = new EditPasswordPresenter(this, this, currentEntry, ownerEmail, realIndex);

        // Precargar datos
        etAppName.setText(currentEntry.getAppName());
        etUsername.setText(currentEntry.getUsername());
        etPassword.setText(currentEntry.getPassword());

        // Spinner categorías
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Red Social", "Banco", "Entretenimiento", "Correo", "Educativo", "Otro"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(currentEntry.getCategory().ordinal());

        btnSave.setOnClickListener(v -> {
            String appName = etAppName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            PasswordEntry.Category category = PasswordEntry.Category.values()[spinnerCategory.getSelectedItemPosition()];

            presenter.saveChanges(appName, username, password, category);
        });

        btnBack.setOnClickListener(v -> finish());
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