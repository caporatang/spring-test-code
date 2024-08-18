package com.example.springtest.testGrammar.sliceTest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * packageName : com.example.springtest.sliceTest.response
 * fileName : GreetingResponse
 * author : taeil
 * date : 6/12/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 6/12/24        taeil                   최초생성
 */
@Data
@RequiredArgsConstructor
public class GreetingResponse {
    private final String message;
    private final Long age;
    private final String who;
}