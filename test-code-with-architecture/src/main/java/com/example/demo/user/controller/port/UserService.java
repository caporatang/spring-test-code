package com.example.demo.user.controller.port;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName : UserService
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public interface UserService {

    User getByEmail(String email);
    User getById(long id);
    User create(UserCreate userCreate);
    User update(long id, UserUpdate userUpdate);
    void login(long id);
    void verifyEmail(long id, String certificationCode);
}