package com.hexa.pecheur_du_dimanche.utils;

/**
 * Use to store the API addresses.
 */
public abstract class Constants {
    public static final String WATER_TEMP_STATIONS_URL = "https://hubeau.eaufrance.fr/api/v1/temperature/station";
    public static final String WATER_TEMP_CHRONIQUE_URL = "https://hubeau.eaufrance.fr/api/v1/temperature/chronique";
    public static final String WATER_QUALITY_ENV_PC_URL = "https://hubeau.eaufrance.fr/api/v1/qualite_rivieres/condition_environnementale_pc";
    public static final String WATER_HYDROMETRY_OBSERVATIONS_URL = "https://hubeau.eaufrance.fr/api/v1/hydrometrie/observations_tr";
    public static final String WATER_FISH_STATE_URL = "https://hubeau.eaufrance.fr/api/v0/etat_piscicole/poissons";
    public static final String API_ADRESSE_URL = "https://api-adresse.data.gouv.fr/reverse";
}
