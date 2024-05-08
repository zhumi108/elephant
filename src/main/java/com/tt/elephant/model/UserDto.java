package com.tt.elephant.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String userId;
    private String emailAddress;
    private String nickname;
    private String password;
    private String avatarURL;
    private int status;//0 logout;1normal;


}
