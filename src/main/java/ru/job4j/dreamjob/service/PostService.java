package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.time.LocalDateTime;
import java.util.Collection;

public class PostService {
    private final PostStore postStore = PostStore.instOf();

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
