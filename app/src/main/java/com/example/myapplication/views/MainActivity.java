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
import com.example.myapplication.presenters.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private Button btnComenzar;
    private Button btnVerCuentas;
    private Button btnAdministrar;
    private TextView tvBienvenido;
    private TextView tvDescripcion;
    private ActivityResultLauncher<Intent> pinLauncher;
    private MainPresenter presenter;

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

        // Crear Presenter y pasarle this como View
        presenter = new MainPresenter(this, this);

        // Launcher para PIN/huella (solo para "Ver Cuentas")
        pinLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = new Intent(MainActivity.this, PasswordListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                    }
                });

        // Cargar estado inicial
        presenter.updateUI();

        btnComenzar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnVerCuentas.setOnClickListener(v -> {
            requestPinAuthentication();
        });

        btnAdministrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.updateUI();  // Refrescar desde Presenter
    }

    // Implementación de la interfaz View del Presenter
    @Override
    public void showRegisteredState(String welcomeText) {
        tvBienvenido.setText(welcomeText);
        tvBienvenido.setTextColor(getResources().getColor(R.color.mint_green));

        btnComenzar.setVisibility(View.GONE);
        btnVerCuentas.setVisibility(View.VISIBLE);
        btnAdministrar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInitialState(String welcomeText) {
        tvBienvenido.setText(welcomeText);
        tvBienvenido.setTextColor(getResources().getColor(R.color.primary_blue)); // o el color original

        btnComenzar.setVisibility(View.VISIBLE);
        btnVerCuentas.setVisibility(View.GONE);
        btnAdministrar.setVisibility(View.GONE);
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