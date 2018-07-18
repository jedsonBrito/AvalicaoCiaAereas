package com.jedsonbrito.avalicaociaaereas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.jedsonbrito.appHelper.AppHelper;
import com.jedsonbrito.config.ConfiguracaoFirebase;
import com.jedsonbrito.model.Usuario;


public class LoginActivity extends AppCompatActivity {

    private EditText edit_email;
    private EditText edit_password;
    private Button btn_logar;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.editTextEmail);
        edit_password = findViewById(R.id.editTextPassword);
        btn_logar = findViewById(R.id.btn_logar);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AppHelper.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE))){
                    usuario = new Usuario();
                    usuario.setEmail(edit_email.getText().toString());
                    usuario.setSenha(edit_password.getText().toString());
                    if(isUsuarioValido()){
                        validarLogin();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Você deve se conectar a internet antes de efetuar o cadastro ",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean isUsuarioValido() {
        boolean usuarioValido = false;
        if(edit_email.getText().toString().isEmpty() || edit_password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Email e senha devem ser preenchidos ",Toast.LENGTH_LONG).show();
        } else {
            usuarioValido = true;
        }

        return usuarioValido;
    }

    private void validarLogin() {

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() && auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this ,"Login realizado com sucesso" ,Toast.LENGTH_SHORT);
                        abrirTelaprincipal();

                } else {

                    String erroExcecao = " ";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail não existe ou foi desabilitado.";
                    } catch (FirebaseAuthInvalidCredentialsException f) {
                        erroExcecao = "Senha invalida";
                    } catch (Exception e) {
                        erroExcecao = "Falha ao realzar o login";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro: "+erroExcecao,Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void abrirTelaprincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuario.class);
        startActivity(intent);

    }

}


