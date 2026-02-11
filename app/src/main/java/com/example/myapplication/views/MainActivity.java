package com.example.myapplication.views;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.managers.LoginSingleton;
import com.example.myapplication.managers.UserManager;

public class MainActivity extends AppCompatActivity {

    private Button btnComenzar;
    private Button btnVerCuentas;
    private Button btnAdministrar;
    private TextView tvBienvenido;
    private TextView tvDescripcion;
    private ActivityResultLauncher<Intent> pinLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlazar vistas
        tvBienvenido = findViewById(R.id.tvBienvenido);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        btnComenzar = findViewById(R.id.btnComenzar);
        btnVerCuentas = findViewById(R.id.btnVerCuentas);
        btnAdministrar = findViewById(R.id.btnAdministrar);

        // Launcher para PIN/huella (solo para "Ver Cuentas")
        pinLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // PIN correcto → abrir PasswordListActivity
                        Intent intent = new Intent(MainActivity.this, PasswordListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                    }
                });

        updateUI();

        btnComenzar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnVerCuentas.setOnClickListener(v -> {
            requestPinAuthentication();  // pide PIN ANTES de cambiar pantalla
        });

        btnAdministrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        boolean isRegistered = LoginSingleton.getInstance().isRegistered();

        if (isRegistered) {
            // Obtener el nombre del usuario actual (logueado automáticamente al registrar)
            String name = "";
            if (UserManager.getInstance().getCurrentUser() != null) {
                name = UserManager.getInstance().getCurrentUser().getName();
            }

            // Texto personalizado con nombre + color verde menta
            tvBienvenido.setText("¡Bienvenido de nuevo, " + name + "!");
            tvBienvenido.setTextColor(getResources().getColor(R.color.mint_green));

            btnComenzar.setVisibility(View.GONE);
            btnVerCuentas.setVisibility(View.VISIBLE);
            btnAdministrar.setVisibility(View.VISIBLE);
        } else {
            tvBienvenido.setText("Bienvenido");
            tvBienvenido.setTextColor(getResources().getColor(R.color.primary_blue));
            btnComenzar.setVisibility(View.VISIBLE);
            btnVerCuentas.setVisibility(View.GONE);
            btnAdministrar.setVisibility(View.GONE);
        }
    }

    private void requestPinAuthentication() {
        KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguard.isDeviceSecure()) {
            Intent intent = keyguard.createConfirmDeviceCredentialIntent("Confirma tu PIN", "Para ver tus cuentas");
            if (intent != null) {
                pinLauncher.launch(intent);
            } else {
                Toast.makeText(this, "No hay PIN configurado en el dispositivo", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Configura un PIN o huella en tu dispositivo para mayor seguridad", Toast.LENGTH_LONG).show();
        }
    }
}