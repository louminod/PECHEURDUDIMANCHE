package com.hexa.pecheur_du_dimanche.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Adresse {
    private String label;
    private String housenumber;
    private String name;
    private String postcode;
    private String citycode;
    private String x;
    private String y;
    private String city;
    private String context;
    private String street;

    public Adresse(JSONObject json) throws JSONException {
        this.label = json.getString("label");
        this.housenumber = json.getString("housenumber");
        this.name = json.getString("name");
        this.postcode = json.getString("postcode");
        this.citycode = json.getString("citycode");
        this.x = json.getString("x");
        this.y = json.getString("y");
        this.city = json.getString("city");
        this.context = json.getString("context");
        this.street = json.getString("street");
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
