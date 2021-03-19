package com.hexa.pecheur_du_dimanche.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Station implements Serializable {
    private String codeStation;
    private String libelleStation;
    private String uriStation;
    private String localisation;
    private double coordonneeX;
    private double coordonneeY;
    private int codeTypeProjection;
    private double longitude;
    private double latitude;
    private String codeCommune;
    private String libelleCommune;
    private String codeDepartement;
    private String libelleDepartement;
    private String codeRegion;
    private String libelleRegion;
    private String codeTronconHydro;
    private String codeCoursEau;
    private String libelleCoursEau;
    private String uriCoursEau;
    private String codeMasseEau;
    private String libelleMasseEau;
    private String uriMasseEau;
    private String codeSousBassin;
    private String libelleSousBassin;
    private String codeBassin;
    private String libelleBassin;
    private String uriBassin;
    private String pk;
    private double altitude;
    private LocalDate dateMajInfos;

    private List<Chronique> chroniqueList;
    private List<EnvironmentalCondition> environmentalConditionList;
    private List<HydrometryObservation> hydrometryObservationList;
    private List<FishState> fishStateList;

    public Station(JSONObject json) throws JSONException {
        this.codeStation = json.getString("code_station");
        this.libelleStation = json.getString("libelle_station");
        this.uriStation = json.getString("uri_station");
        this.localisation = json.getString("localisation");
        this.coordonneeX = json.getDouble("coordonnee_x");
        this.coordonneeY = json.getDouble("coordonnee_y");
        this.codeTypeProjection = json.getInt("code_type_projection");
        this.longitude = json.getDouble("longitude");
        this.latitude = json.getDouble("latitude");
        this.codeCommune = json.getString("code_commune");
        this.libelleCommune = json.getString("libelle_commune");
        this.codeDepartement = json.getString("code_departement");
        this.libelleDepartement = json.getString("libelle_departement");
        this.codeRegion = json.getString("code_region");
        this.libelleRegion = json.getString("libelle_region");
        this.codeTronconHydro = json.getString("code_troncon_hydro");
        this.codeCoursEau = json.getString("code_cours_eau");
        this.libelleCoursEau = json.getString("libelle_cours_eau");
        this.uriCoursEau = json.getString("uri_cours_eau");
        this.codeMasseEau = json.getString("code_masse_eau");
        this.libelleMasseEau = json.getString("libelle_masse_eau");
        this.uriMasseEau = json.getString("uri_masse_eau");
        this.codeSousBassin = json.getString("code_sous_bassin");
        this.libelleSousBassin = json.getString("libelle_sous_bassin");
        this.codeBassin = json.getString("code_bassin");
        this.libelleBassin = json.getString("libelle_bassin");
        this.uriBassin = json.getString("uri_bassin");
        this.pk = json.getString("pk");
        if (!json.isNull("altitude")) {
            this.altitude = json.getDouble("altitude");
        }

        this.dateMajInfos = LocalDate.parse(json.getString("date_maj_infos"));
    }

    public List<Chronique> getChroniqueList() {
        return chroniqueList;
    }

    public void setChroniqueList(List<Chronique> chroniqueList) {
        this.chroniqueList = chroniqueList;
    }

    public List<EnvironmentalCondition> getEnvironmentalConditionList() {
        return environmentalConditionList;
    }

    public void setEnvironmentalConditionList(List<EnvironmentalCondition> environmentalConditionList) {
        this.environmentalConditionList = environmentalConditionList;
    }

    public List<HydrometryObservation> getHydrometryObservationList() {
        return hydrometryObservationList;
    }

    public void setHydrometryObservationList(List<HydrometryObservation> hydrometryObservationList) {
        this.hydrometryObservationList = hydrometryObservationList;
    }

    public List<FishState> getFishStateList() {
        return fishStateList;
    }

    public void setFishStateList(List<FishState> fishStateList) {
        this.fishStateList = fishStateList;
    }

    public String getCodeStation() {
        return codeStation;
    }

    public void setCodeStation(String codeStation) {
        this.codeStation = codeStation;
    }

    public String getLibelleStation() {
        return libelleStation;
    }

    public void setLibelleStation(String libelleStation) {
        this.libelleStation = libelleStation;
    }

    public String getUriStation() {
        return uriStation;
    }

    public void setUriStation(String uriStation) {
        this.uriStation = uriStation;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public double getCoordonneeX() {
        return coordonneeX;
    }

    public void setCoordonneeX(double coordonneeX) {
        this.coordonneeX = coordonneeX;
    }

    public double getCoordonneeY() {
        return coordonneeY;
    }

    public void setCoordonneeY(double coordonneeY) {
        this.coordonneeY = coordonneeY;
    }

    public int getCodeTypeProjection() {
        return codeTypeProjection;
    }

    public void setCodeTypeProjection(int codeTypeProjection) {
        this.codeTypeProjection = codeTypeProjection;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getLibelleCommune() {
        return libelleCommune;
    }

    public void setLibelleCommune(String libelleCommune) {
        this.libelleCommune = libelleCommune;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getLibelleDepartement() {
        return libelleDepartement;
    }

    public void setLibelleDepartement(String libelleDepartement) {
        this.libelleDepartement = libelleDepartement;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }

    public String getLibelleRegion() {
        return libelleRegion;
    }

    public void setLibelleRegion(String libelleRegion) {
        this.libelleRegion = libelleRegion;
    }

    public String getCodeTronconHydro() {
        return codeTronconHydro;
    }

    public void setCodeTronconHydro(String codeTronconHydro) {
        this.codeTronconHydro = codeTronconHydro;
    }

    public String getCodeCoursEau() {
        return codeCoursEau;
    }

    public void setCodeCoursEau(String codeCoursEau) {
        this.codeCoursEau = codeCoursEau;
    }

    public String getLibelleCoursEau() {
        return libelleCoursEau;
    }

    public void setLibelleCoursEau(String libelleCoursEau) {
        this.libelleCoursEau = libelleCoursEau;
    }

    public String getUriCoursEau() {
        return uriCoursEau;
    }

    public void setUriCoursEau(String uriCoursEau) {
        this.uriCoursEau = uriCoursEau;
    }

    public String getCodeMasseEau() {
        return codeMasseEau;
    }

    public void setCodeMasseEau(String codeMasseEau) {
        this.codeMasseEau = codeMasseEau;
    }

    public String getLibelleMasseEau() {
        return libelleMasseEau;
    }

    public void setLibelleMasseEau(String libelleMasseEau) {
        this.libelleMasseEau = libelleMasseEau;
    }

    public String getUriMasseEau() {
        return uriMasseEau;
    }

    public void setUriMasseEau(String uriMasseEau) {
        this.uriMasseEau = uriMasseEau;
    }

    public String getCodeSousBassin() {
        return codeSousBassin;
    }

    public void setCodeSousBassin(String codeSousBassin) {
        this.codeSousBassin = codeSousBassin;
    }

    public String getLibelleSousBassin() {
        return libelleSousBassin;
    }

    public void setLibelleSousBassin(String libelleSousBassin) {
        this.libelleSousBassin = libelleSousBassin;
    }

    public String getCodeBassin() {
        return codeBassin;
    }

    public void setCodeBassin(String codeBassin) {
        this.codeBassin = codeBassin;
    }

    public String getLibelleBassin() {
        return libelleBassin;
    }

    public void setLibelleBassin(String libelleBassin) {
        this.libelleBassin = libelleBassin;
    }

    public String getUriBassin() {
        return uriBassin;
    }

    public void setUriBassin(String uriBassin) {
        this.uriBassin = uriBassin;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public LocalDate getDateMajInfos() {
        return dateMajInfos;
    }

    public void setDateMajInfos(LocalDate dateMajInfos) {
        this.dateMajInfos = dateMajInfos;
    }
}
