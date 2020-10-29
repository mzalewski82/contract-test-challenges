package com.example.multipleoutput;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdditionalInputMessage {

    private final String aText;

    @JsonCreator
    public AdditionalInputMessage(@JsonProperty("aText") String aText) {
        this.aText = aText;
    }

    public String getaText() {
        return aText;
    }
}
