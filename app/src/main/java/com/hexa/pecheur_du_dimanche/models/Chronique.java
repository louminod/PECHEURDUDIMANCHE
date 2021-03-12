package com.hexa.pecheur_du_dimanche.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chronique {
    private LocalDateTime dateHeureMesure;
    private double resultat;
    private String codeUnite;
    private String symboleUnite;
    private String code_qualification;
    private String libelleQualification;

    public Chronique(JSONObject json) throws JSONException {
        if (!json.isNull("date_mesure_temp") && !json.isNull("heure_mesure_temp")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.dateHeureMesure = LocalDateTime.parse(json.getString("date_mesure_temp") + " " + json.getString("heure_mesure_temp"), formatter);
        }

        this.resultat = json.getDouble("resultat");
        this.codeUnite = json.getString("code_unite");
        this.symboleUnite = json.getString("symbole_unite");
        this.code_qualification = json.getString("code_qualification");
        this.libelleQualification = json.getString("libelle_qualification");
    }

    public LocalDateTime getDateHeureMesure() {
        return dateHeureMesure;
    }

    public void setDateHeureMesure(LocalDateTime dateHeureMesure) {
        this.dateHeureMesure = dateHeureMesure;
    }

    public double getResultat() {
        return resultat;
    }

    public void setResultat(double resultat) {
        this.resultat = resultat;
    }

    public String getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(String codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getSymboleUnite() {
        return symboleUnite;
    }

    public void setSymboleUnite(String symboleUnite) {
        this.symboleUnite = symboleUnite;
    }

    public String getCode_qualification() {
        return code_qualification;
    }

    public void setCode_qualification(String code_qualification) {
        this.code_qualification = code_qualification;
    }

    public String getLibelleQualification() {
        return libelleQualification;
    }

    public void setLibelleQualification(String libelleQualification) {
        this.libelleQualification = libelleQualification;
    }
}
