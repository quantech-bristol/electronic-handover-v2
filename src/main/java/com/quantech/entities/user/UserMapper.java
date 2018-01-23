package com.quantech.entities.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserCore newUser = new UserCore();
        newUser.setPassword(rs.getString("password"));
        newUser.setUsername(rs.getString("username"));
        return newUser;
    }
}
