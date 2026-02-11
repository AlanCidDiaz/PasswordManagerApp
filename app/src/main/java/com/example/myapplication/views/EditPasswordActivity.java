package com.example.myapplication.views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.managers.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import java.util.Date;
import java.util.List;

public class EditPasswordActivity extends AppCompatActivity {

    private TextInputEditText etAppName, etUsername, etPassword;
    private Spinner spinnerCategory;
    private PasswordEntry currentEntry;
    private String ownerEmail;
    private int realIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        etAppName    = findViewById(R.id.etAppName);
        etUsername   = findViewById(R.id.etUsername);
        etPassword   = findViewById(R.id.etPassword);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        MaterialButton btnSave = findViewById(R.id.btnSavePassword);
        MaterialButton btnBack = findViewById(R.id.btnBack);

        currentEntry = getIntent().getParcelableExtra("password_entry");
        ownerEmail   = getIntent().getStringExtra("owner_email");
        realIndex    = getIntent().getIntExtra("real_index", -1);

        if (currentEntry == null || ownerEmail == null || realIndex == -1) {
            Toast.makeText(this, "Error al cargar cuenta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
            String appName   = etAppName.getText().toString().trim();
            String username  = etUsername.getText().toString().trim();
            String password  = etPassword.getText().toString().trim();
            PasswordEntry.Category category = PasswordEntry.Category.values()[spinnerCategory.getSelectedItemPosition()];

            if (appName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear entrada actualizada → pero MANTENER createdAt y ACTUALIZAR updatedAt
            PasswordEntry updatedEntry = new PasswordEntry(appName, username, password, category);
            updatedEntry.setCreatedAt(currentEntry.getCreatedAt());           // ← importante
            updatedEntry.setUpdatedAt(new Date());                            // ← ahora

            // Guardar en la lista del dueño
            List<PasswordEntry> list = UserManager.getInstance().getPasswords(ownerEmail);
            if (realIndex >= 0 && realIndex < list.size()) {
                list.set(realIndex, updatedEntry);
                Toast.makeText(this, "Cuenta actualizada", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error: posición inválida", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}