package com.example.demo.post.service.port;

import com.example.demo.post.infrastructure.PostEntity;

import java.util.Optional;

/**
 * packageName : com.example.demo.post.service.port
 * fileName : PostRepository
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public interface PostRepository {
    Optional<PostEntity> findById(long id);

    PostEntity save(PostEntity postEntity);

}