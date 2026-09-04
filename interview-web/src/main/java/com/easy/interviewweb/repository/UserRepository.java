package com.easy.interviewweb.repository;

import com.easy.interviewweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from user where status=:status", nativeQuery = true)
    List<User> listUserByStatus(@Param("status") Integer status);

}
