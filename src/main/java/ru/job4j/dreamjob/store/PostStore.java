package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostStore {
    private static final PostStore INST = new PostStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static int id = 4;
    
    public PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Write java code as junior", LocalDate.now().toString()));
        posts.put(2, new Post(2, "Middle Java Job", "Write java code as middle", LocalDate.now().toString()));
        posts.put(3, new Post(3, "Senior Java Job", "Write java code as senior", LocalDate.now().toString()));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        posts.putIfAbsent(id++, post);
    }
}
