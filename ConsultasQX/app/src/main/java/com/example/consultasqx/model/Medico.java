package com.example.consultasqx.model;

import java.util.List;

public class Medico{

    private String id;
    private String crm;
    private String nome;
    private String cpf;
    private String nome_clinica;
    private List<String> especialidades;
    private List<String> convenios;
    private List<String> datas;
    private List<String> horarios;
    private String longitude;
    private String latitute;


    public Medico(String id, String nome, String crm){
        this.id = id;
        this.nome = nome;
        this.crm = crm;
    }

    public Medico(String id, String nome, String crm, String cpf, String nome_clinica, List<String> especialidade, List<String> convenio, String latitute, String longitude) {
        this.id = id;
        this.crm = crm;
        this.nome = nome;
        this.cpf = cpf;
        this.nome_clinica = nome_clinica;
        this.convenios= convenio;
        this.especialidades = especialidade;
        this.latitute = latitute;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome_clinica() {
        return nome_clinica;
    }

    public void setNome_clinica(String nome_clinica) {
        this.nome_clinica = nome_clinica;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public List<String> getConvenios() {
        return convenios;
    }

    public void setConvenios(List<String> convenios) {
        this.convenios = convenios;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", crm='" + crm + '\'' +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + nome_clinica + '\'' +
                ", convenio='" + convenios + '\'' +
                ", especialidade='" + especialidades + '\'' +
                '}';
    }
}
