package com.tt.elephant.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenModel implements Serializable {
    private String  userId;
    private String token;

    public TokenModel(String userId , String token){
        this.userId = userId;
        this.token = token;
    }

}