package com.project.dailykids.models;

import java.io.Serializable;

public class KinderInfo implements Serializable {
    private String kinderName;
    private String kinderAddress;
    private String kinderTel;

    public String getKinderName() {
        return kinderName;
    }

    public String getKinderAddress() {
        return kinderAddress;
    }

    public String getKinderTel() {
        return kinderTel;
    }

    public KinderInfo() {
    }

    public KinderInfo(String kinderName, String kinderAddress, String kinderTel) {
        this.kinderName = kinderName;
        this.kinderAddress = kinderAddress;
        this.kinderTel = kinderTel;
    }
}
