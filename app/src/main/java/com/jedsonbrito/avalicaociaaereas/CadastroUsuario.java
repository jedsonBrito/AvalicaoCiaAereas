package com.jedsonbrito.avalicaociaaereas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.jedsonbrito.appHelper.AppHelper;
import com.jedsonbrito.config.ConfiguracaoFirebase;
import com.jedsonbrito.model.Usuario;
import com.vicmikhailau.maskededittext.MaskedFormatter;
import com.vicmikhailau.maskededittext.MaskedWatcher;

public class CadastroUsuario extends AppCompatActivity {

    private EditText edt_nome;
    private EditText edt_email;
    private EditText edt_senha;
    private RadioButton rdb_masc;
    private RadioButton rdb_fem;
    private EditText edt_conf_senha;
    private EditText edt_data_nascimento;
    private Button btn_cadastrar;
    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edt_nome = findViewById(R.id.edit_cadastro_nome);
        edt_conf_senha = findViewById(R.id.confir_senha);
        edt_data_nascimento = findViewById(R.id.edit_data_nascimento);
        edt_email = findViewById(R.id.edit_cadastro_email);
        edt_senha = findViewById(R.id.edit_cadastro_senha);
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        rdb_masc = findViewById(R.id.rdb_masc);
        rdb_fem = findViewById(R.id.rdb_fem);
        edt_conf_senha = findViewById(R.id.confir_senha);

        MaskedFormatter formatter = new MaskedFormatter("##/##/####");
        edt_data_nascimento.addTextChangedListener(new MaskedWatcher(formatter, edt_data_nascimento));

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(AppHelper.isConnected(getSystemService(Context.CONNECTIVITY_SERVICE)) && isvalidaInformacao()){
                    usuario = new Usuario();
                    usuario.setEmail(edt_email.getText().toString());
                    usuario.setNome(edt_nome.getText().toString());
                    usuario.setSenha(edt_senha.getText().toString());
                    usuario.setDataNascimento(edt_data_nascimento.getText().toString());

                    if(rdb_fem.isChecked()){
                        usuario.setSexo("Feminino");
                    }

                    if(rdb_masc.isChecked()){
                        usuario.setSexo("Masculino");
                    }

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

                    FirebaseUser userFirebase = task.getResult().getUser();
                    usuario.setId( userFirebase.getUid());
                    enviarEmailUsuario(userFirebase);
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

    private void enviarEmailUsuario(final FirebaseUser userFirebase){
        final String email = userFirebase.getEmail();
        userFirebase.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuario.this, "E-mail enviado para "+email,Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CadastroUsuario.this, "Falha ao enviar o E-mail ",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isvalidaInformacao(){
        boolean info = false;
        //Valida Senha
        if(edt_senha.getText().toString().equals(edt_conf_senha.getText().toString())){
            info = true;
        } else {
            Toast.makeText(CadastroUsuario.this, "Senha e confirmação de senha devem ser iguais",Toast.LENGTH_SHORT).show();
        }
        return info;
    }

}
