package com.jedsonbrito.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.jedsonbrito.config.ConfiguracaoFirebase;

public class Usuario {

    private String id;
    private String senha;
    private String email;
    private String nome;

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("usuarios").child( getId() ).setValue(this);
    }

    public Usuario() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
