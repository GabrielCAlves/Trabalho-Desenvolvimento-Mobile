package com.example.consultasqx.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;


public class Usuario {
    @Exclude
    private String key;
    private String nome, email, senha, cpf, telefone, papel, crm, nomeDaClinica, teleDaClinica, coordX, coordY;
    private ArrayList<String> especialidades, convenios;
    private String id;

    public Usuario(int id, String nome, String email, String senha, String cpf, String telefone, String papel, ArrayList<String> especialidades, ArrayList<String> convenios, String crm, String nomeDaClinica, String teleDaClinica, String coordX, String coordY) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.papel = papel;
        this.especialidades = especialidades;
        this.convenios = convenios;
        this.crm = crm;
        this.nomeDaClinica = nomeDaClinica;
        this.teleDaClinica = teleDaClinica;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public Usuario() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public ArrayList<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<String> especialidades) {
        this.especialidades = especialidades;
    }

    public ArrayList<String> getConvenios() {
        return convenios;
    }

    public void setConvenios(ArrayList<String> convenios) {
        this.convenios = convenios;
    }
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNomeDaClinica() {
        return nomeDaClinica;
    }

    public void setNomeDaClinica(String nomeDaClinica) {
        this.nomeDaClinica = nomeDaClinica;
    }

    public String getTeleDaClinica() {
        return teleDaClinica;
    }

    public void setTeleDaClinica(String teleDaClinica) {
        this.teleDaClinica = teleDaClinica;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }

}
