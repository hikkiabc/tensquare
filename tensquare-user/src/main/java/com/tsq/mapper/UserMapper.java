package com.tsq.mapper;

import com.tsq.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMapper  extends JpaRepository<User,String> {

}
