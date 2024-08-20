package com.example.demo.post.controller.port;

import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

/**
 * packageName : com.example.demo.post.controller.port
 * fileName : PostService
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
public interface PostService {

    Post getById(long id);
    Post create(PostCreate postCreate);
    Post update(long id, PostUpdate postUpdate);

}