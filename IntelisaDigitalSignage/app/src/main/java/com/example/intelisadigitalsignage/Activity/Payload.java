package com.example.intelisadigitalsignage.Activity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Payload implements Serializable {

    Payload(String message) {
        this.message = message;
    }


    String message;
}
