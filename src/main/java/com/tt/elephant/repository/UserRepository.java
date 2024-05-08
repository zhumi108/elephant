package com.tt.elephant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query(value = "select * from user as y where y.email_address=?1 and y.password=?2 and y.status=1",nativeQuery = true)
    UserEntity findByEmailAddressPassword(String emailAddress, String password);

    @Query(value="select count(user_id) from user where nickname=?1 and status=1",nativeQuery = true)
    Integer findByNickname(String nickname);

    @Query(value="select count(user_id) from user where email_address=?1 and status=1",nativeQuery = true)
    Integer findByEmailAddress(String emailAddress);

    @Query(value="update user set status =?1 where user_id=?2",nativeQuery = true)
    Integer updateStatus(int status,String userId);



}
