package com.easy.interviewapi.repository;

import com.easy.interviewapi.model.UserInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserInfoRepository {

    private static final RowMapper<UserInfo> ROW_MAPPER = (rs, rowNum) -> UserInfo.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .role(rs.getString("role"))
            .build();

    private final JdbcTemplate jdbcTemplate;

    public UserInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserInfo> findAll() {
        return jdbcTemplate.query(
                "SELECT id, name, email, role FROM user_info ORDER BY id",
                ROW_MAPPER
        );
    }

    public Optional<UserInfo> findById(long id) {
        List<UserInfo> rows = jdbcTemplate.query(
                "SELECT id, name, email, role FROM user_info WHERE id = ?",
                ROW_MAPPER,
                id
        );
        return rows.stream().findFirst();
    }
}
