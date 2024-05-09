package com.tt.elephant.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "user")
@Setter
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "org.hibernate.id.UUIDGenerator")
    private String userId;

    private String emailAddress;
    private String nickname;
    private String password;
    private String avatarUrl;
    private String token;
    private long createTime;
    private long updateTime;

    @Column(columnDefinition = "INT DEFAULT 1")
    private int status;
}
