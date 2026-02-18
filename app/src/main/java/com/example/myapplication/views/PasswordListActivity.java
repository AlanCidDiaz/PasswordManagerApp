package com.example.myapplication.views;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.PasswordEntry;
import com.example.myapplication.presenters.PasswordListPresenter;

import java.util.ArrayList;
import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView recyclerPasswords;
    private TextView tvEmpty;
    private TextView tvTitle;
    private PasswordAdapter adapter;
    private PasswordListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        recyclerPasswords = findViewById(R.id.recyclerPasswords);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvTitle = findViewById(R.id.tvTitle);

        if (tvTitle != null) {
            tvTitle.setText("Mis Cuentas");
        }

        recyclerPasswords.setLayoutManager(new LinearLayoutManager(this));

        // Crear Presenter y pasarle this como contexto
        presenter = new PasswordListPresenter(this);

        // Adapter en modo normal (null listener)
        adapter = new PasswordAdapter(new ArrayList<>(), false, null);
        recyclerPasswords.setAdapter(adapter);

        loadPasswords();
    }

    private void loadPasswords() {
        List<PasswordEntry> passwords = presenter.loadPasswords();

        if (passwords.isEmpty()) {
            tvEmpty.setText(presenter.getEmptyMessage());
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerPasswords.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerPasswords.setVisibility(View.VISIBLE);
            adapter.updateList(passwords);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords();
    }
}