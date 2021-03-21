package com.hexa.pecheur_du_dimanche.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HydrometryObservation implements Serializable {
    private String grandeurHydro;
    private int statutSerie;
    private int codeSystemeAltiSerie;
    private LocalDateTime dateObs;
    private double resultatObs;
    private int codeMethodeObs;
    private String libelleMethodeObs;
    private String codeQualificationObs;
    private String libelleQualificationObs;

    /**
     * Custom constructor converting a Json to this class object
     * @param json
     * @throws JSONException
     */
    public HydrometryObservation(JSONObject json) throws JSONException {
        if (!json.isNull("date_obs")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.dateObs = LocalDateTime.parse(json.getString("date_obs").replace("T", " ").replace("Z", ""), formatter);
        }

        this.grandeurHydro = json.getString("grandeur_hydro");
        this.statutSerie = json.getInt("statut_serie");
        this.codeSystemeAltiSerie = json.getInt("code_systeme_alti_serie");
        this.resultatObs = json.getDouble("resultat_obs");
        this.codeMethodeObs = json.getInt("code_methode_obs");
        this.libelleMethodeObs = json.getString("libelle_methode_obs");
        this.codeQualificationObs = json.getString("code_qualification_obs");
        this.libelleQualificationObs = json.getString("libelle_qualification_obs");
    }

    public String getGrandeurHydro() {
        return grandeurHydro;
    }

    public void setGrandeurHydro(String grandeurHydro) {
        this.grandeurHydro = grandeurHydro;
    }

    public int getStatutSerie() {
        return statutSerie;
    }

    public void setStatutSerie(int statutSerie) {
        this.statutSerie = statutSerie;
    }

    public int getCodeSystemeAltiSerie() {
        return codeSystemeAltiSerie;
    }

    public void setCodeSystemeAltiSerie(int codeSystemeAltiSerie) {
        this.codeSystemeAltiSerie = codeSystemeAltiSerie;
    }

    public LocalDateTime getDateObs() {
        return dateObs;
    }

    public void setDateObs(LocalDateTime dateObs) {
        this.dateObs = dateObs;
    }

    public double getResultatObs() {
        return resultatObs;
    }

    public void setResultatObs(double resultatObs) {
        this.resultatObs = resultatObs;
    }

    public int getCodeMethodeObs() {
        return codeMethodeObs;
    }

    public void setCodeMethodeObs(int codeMethodeObs) {
        this.codeMethodeObs = codeMethodeObs;
    }

    public String getLibelleMethodeObs() {
        return libelleMethodeObs;
    }

    public void setLibelleMethodeObs(String libelleMethodeObs) {
        this.libelleMethodeObs = libelleMethodeObs;
    }

    public String getCodeQualificationObs() {
        return codeQualificationObs;
    }

    public void setCodeQualificationObs(String codeQualificationObs) {
        this.codeQualificationObs = codeQualificationObs;
    }

    public String getLibelleQualificationObs() {
        return libelleQualificationObs;
    }

    public void setLibelleQualificationObs(String libelleQualificationObs) {
        this.libelleQualificationObs = libelleQualificationObs;
    }
}
