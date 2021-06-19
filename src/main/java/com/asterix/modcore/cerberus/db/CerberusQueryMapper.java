package com.asterix.modcore.cerberus.db;

import org.apache.ibatis.annotations.Select;

public interface CerberusQueryMapper {

    @Select("SELECT password FROM asterix_user where username = #{username}")
    String selectUserPassword(String username);
}
