package com.example.demo.post.infrastructure;

import com.example.demo.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName : com.example.demo.post.infrastructure
 * fileName : PostRepositoryImpl
 * author : taeil
 * date : 8/20/24
 * description :
 * =======================================================
 * DATE          AUTHOR                      NOTE
 * -------------------------------------------------------
 * 8/20/24        taeil                   최초생성
 */
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Optional<PostEntity> findById(long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public PostEntity save(PostEntity postEntity) {
        return postJpaRepository.save(postEntity);
    }
}