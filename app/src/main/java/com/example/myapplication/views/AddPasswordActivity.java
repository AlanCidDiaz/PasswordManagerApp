package com.example.myapplication.views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.presenters.AddPasswordPresenter;
import com.example.myapplication.models.PasswordEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

public class AddPasswordActivity extends AppCompatActivity {

    private TextInputEditText etAppName, etUsername, etPassword;
    private Spinner spinnerCategory;
    private AddPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        // Inicializar views
        etAppName = findViewById(R.id.etAppName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        MaterialButton btnSave = findViewById(R.id.btnSavePassword);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        presenter = new AddPasswordPresenter(this);

        // Configurar Spinner con categorías
        PasswordEntry.Category[] categories = PasswordEntry.Category.values();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                new String[]{
                        "Red Social", "Banco", "Entretenimiento", "Correo", "Educativo", "Otro"
                });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Posición inicial: Red Social
        spinnerCategory.setSelection(0);

        btnSave.setOnClickListener(v -> {
            String appName = etAppName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            PasswordEntry.Category category = PasswordEntry.Category.values()[spinnerCategory.getSelectedItemPosition()];

            if (presenter.savePassword(appName, username, password, category)) {
                // Regresar a Home después de guardar
                setResult(RESULT_OK);
                finish();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}