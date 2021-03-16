package com.hexa.pecheur_du_dimanche.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FishState {
    private LocalDate dateOperation;
    private String codeEspecePoisson;
    private String nomPoisson;
    private int effectif;
    private double poids;
    private double densite;
    private double surfacePeche;

    public FishState(JSONObject json) throws JSONException {
        if (!json.isNull("date_operation")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.dateOperation = LocalDate.parse(json.getString("date_operation"), formatter);
        }

        this.codeEspecePoisson = json.getString("code_espece_poisson");
        this.nomPoisson = json.getString("nom_poisson");
        this.effectif = json.getInt("effectif");
        this.poids = json.getDouble("poids");
        this.densite = json.getDouble("densite");
        this.surfacePeche = json.getDouble("surface_peche");
    }

    public LocalDate getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDate dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getCodeEspecePoisson() {
        return codeEspecePoisson;
    }

    public void setCodeEspecePoisson(String codeEspecePoisson) {
        this.codeEspecePoisson = codeEspecePoisson;
    }

    public String getNomPoisson() {
        return nomPoisson;
    }

    public void setNomPoisson(String nomPoisson) {
        this.nomPoisson = nomPoisson;
    }

    public int getEffectif() {
        return effectif;
    }

    public void setEffectif(int effectif) {
        this.effectif = effectif;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public double getDensite() {
        return densite;
    }

    public void setDensite(double densite) {
        this.densite = densite;
    }

    public double getSurfacePeche() {
        return surfacePeche;
    }

    public void setSurfacePeche(double surfacePeche) {
        this.surfacePeche = surfacePeche;
    }
}
