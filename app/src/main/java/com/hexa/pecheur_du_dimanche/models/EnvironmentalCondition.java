package com.hexa.pecheur_du_dimanche.models;

import android.icu.util.LocaleData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnvironmentalCondition implements Serializable {
    private LocalDate datePrelevement;
    private String resultat;
    private String codeRemarque;
    private String mnemoRemarque;
    private String codeStatut;
    private String mnemoStatut;
    private String codeQualification;
    private String libelleQualification;
    private String commentaire;
    private String nomPreleveur;

    public EnvironmentalCondition(JSONObject json) throws JSONException {
        if (!json.isNull("date_prelevement")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.datePrelevement = LocalDate.parse(json.getString("date_prelevement"), formatter);
        }

        this.codeRemarque = json.getString("code_remarque");
        this.resultat = json.getString("resultat");
        this.mnemoRemarque = json.getString("mnemo_remarque");
        this.codeStatut = json.getString("code_statut");
        this.mnemoStatut = json.getString("mnemo_statut");
        this.codeQualification = json.getString("code_qualification");
        this.libelleQualification = json.getString("libelle_qualification");
        this.commentaire = json.getString("commentaire");
        this.nomPreleveur = json.getString("nom_preleveur");
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public LocalDate getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(LocalDate datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public String getCodeRemarque() {
        return codeRemarque;
    }

    public void setCodeRemarque(String codeRemarque) {
        this.codeRemarque = codeRemarque;
    }

    public String getMnemoRemarque() {
        return mnemoRemarque;
    }

    public void setMnemoRemarque(String mnemoRemarque) {
        this.mnemoRemarque = mnemoRemarque;
    }

    public String getCodeStatut() {
        return codeStatut;
    }

    public void setCodeStatut(String codeStatut) {
        this.codeStatut = codeStatut;
    }

    public String getMnemoStatut() {
        return mnemoStatut;
    }

    public void setMnemoStatut(String mnemoStatut) {
        this.mnemoStatut = mnemoStatut;
    }

    public String getCodeQualification() {
        return codeQualification;
    }

    public void setCodeQualification(String codeQualification) {
        this.codeQualification = codeQualification;
    }

    public String getLibelleQualification() {
        return libelleQualification;
    }

    public void setLibelleQualification(String libelleQualification) {
        this.libelleQualification = libelleQualification;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getNomPreleveur() {
        return nomPreleveur;
    }

    public void setNomPreleveur(String nomPreleveur) {
        this.nomPreleveur = nomPreleveur;
    }
}
