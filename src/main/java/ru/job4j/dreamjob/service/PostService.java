package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class PostService {
    private final PostStore postStore;

    public PostService(PostStore postStore) {
        this.postStore = postStore;
    }

    public void add(Post post) {
        post.setCreated(LocalDateTime.now());
        postStore.add(post);
    }

    public void update(Post post) {
        postStore.update(post);
    }

    public Post findById(int id) {
        return postStore.findById(id);
    }

    public Collection<Post> findAll() {
        return postStore.findAll();
    }
}