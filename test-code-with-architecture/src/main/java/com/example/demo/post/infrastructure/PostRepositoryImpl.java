package com.example.demo.post.infrastructure;

import com.example.demo.post.service.port.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


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
    public Optional<com.example.demo.post.domain.Post> findById(long id) {
        return postJpaRepository.findById(id).map(PostEntity::toModel);
    }

    @Override
    public com.example.demo.post.domain.Post save(com.example.demo.post.domain.Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toModel();
    }
}