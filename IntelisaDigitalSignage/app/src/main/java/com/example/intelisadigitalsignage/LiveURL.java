package com.example.intelisadigitalsignage;

import java.io.Serializable;

public class LiveURL implements Serializable {

    String iteration;
    String adname;

    public LiveURL(String iteration,String adname)
    {
        this.iteration = iteration;
        this.adname = adname;
    }



    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }


    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }


}
