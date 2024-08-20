package com.example.demo.user.controller.port;

/**
 * packageName : com.example.demo.user.controller.port
 * fileName : CertificationService
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public interface CertificationService {
    public void send(String email, long id, String certificationCode);


}