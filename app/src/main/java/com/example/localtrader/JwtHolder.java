package com.example.localtrader;

import androidx.annotation.NonNull;

public class JwtHolder {

    protected String jwtString;

    public JwtHolder(String jwtString) {
        this.jwtString = jwtString;
    }

    @NonNull
    @Override
    public String toString() {
        return this.jwtString;
    }
}
