package com.jedsonbrito.avalicaociaaereas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.jedsonbrito.config.ConfiguracaoFirebase;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference referenciaFireBase;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        referenciaFireBase = ConfiguracaoFirebase.getFirebase();
        referenciaFireBase.child("teste").setValue("100");

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);

    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuario.class);
        startActivity(intent);

    }







}


