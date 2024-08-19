package com.example.demo.user.service.port;

/**
 * packageName : com.example.demo.user.service.port
 * fileName : MailSender
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public interface MailSender {
    void send(String email, String title, String content);

}