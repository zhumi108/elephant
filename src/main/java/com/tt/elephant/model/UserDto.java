package com.tt.elephant.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String userId;
    private String emailAddress;
    private String nickname;
    private String avatarUrl;
    private String token;
    private int status;
    private String blockedUsers;

}
