package com.example.intelisadigitalsignage.Activity;

import java.io.Serializable;

public class Payload implements Serializable {

    Payload(String message) {
        this.message = message;
    }


    String message;
}
