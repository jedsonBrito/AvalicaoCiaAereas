package com.jedsonbrito.avalicaociaaereas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.jedsonbrito.config.ConfiguracaoFirebase;
import com.jedsonbrito.model.Usuario;

public class CadastroUsuario extends AppCompatActivity {

    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_senha;
    private Button btn_cadastrar;
    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edt_nome = findViewById(R.id.edit_cadastro_nome);
        edt_email = findViewById(R.id.edit_cadastro_email);
        edt_senha = findViewById(R.id.edit_cadastro_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isConnected()){
                    usuario = new Usuario();
                    usuario.setEmail(edt_email.getText().toString());
                    usuario.setNome(edt_nome.getText().toString());
                    usuario.setSenha(edt_senha.getText().toString());
                    cadastrarUsuario();

                } else {
                    Toast.makeText(CadastroUsuario.this, "Você deve se conectar a internet antes de efetuar o cadastro ",Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    private void cadastrarUsuario() {
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuario.this, "Sucesso ao Cadastrar usuário",Toast.LENGTH_SHORT).show();

                    FirebaseUser userFirebase = task.getResult().getUser();
                    usuario.setId( userFirebase.getUid());
                    usuario.salvar();

                    auth.signOut();
                    finish();

                } else {

                    String erroExcecao = " ";

                    try{
                       throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte contendo letras e números";
                    } catch (FirebaseAuthUserCollisionException f) {
                        erroExcecao = "Esse e-mail já está em uso no app";
                    } catch (FirebaseAuthInvalidCredentialsException a) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao cadastrar Usuário";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuario.this, "Erro: "+erroExcecao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

        public boolean isConnected() {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                return connected = true;
            }
                return connected;
        }

}
