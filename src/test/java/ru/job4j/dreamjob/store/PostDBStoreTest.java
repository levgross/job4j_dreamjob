package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PostDBStoreTest {
    PostDBStore store = new PostDBStore(new Main().loadPool());

    @Test
    public void whenAddPost() {
        Post post = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post.setCity(new City(0, "NYC"));
        post.setVisible(true);
        store.add(post);
        Post postInDB = store.findById(post.getId());
        assertThat(postInDB.getName()).isEqualTo(post.getName());
        assertThat(postInDB.getDescription()).isEqualTo(post.getDescription());
        assertThat(postInDB.getCreated().getSecond()).isEqualTo(post.getCreated().getSecond());
        assertThat(postInDB.getCity()).isEqualTo(post.getCity());
        assertThat(postInDB.isVisible()).isEqualTo(post.isVisible());
    }

    @Test
    public void whenFindAllPosts() {
        Post post1 = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post1.setCity(new City(0, "NYC"));
        post1.setVisible(true);
        store.add(post1);
        Post post2 = new Post(1, "Java Job Dev", "Description Dev", LocalDateTime.now());
        post2.setCity(new City(1, "Moscow"));
        post2.setVisible(false);
        store.add(post2);
        assertThat(store.findAll()).contains(post1, post2);
    }

    @Test
    public void whenUpdatePost() {
        Post post1 = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post1.setCity(new City(0, "NYC"));
        post1.setVisible(true);
        store.add(post1);
        Post post2 = new Post(post1.getId(), "Java Job Dev", "Description Dev", LocalDateTime.now());
        post2.setCity(new City(1, "Moscow"));
        post2.setVisible(false);
        store.update(post2);
        Post postInDB = store.findById(post1.getId());
        assertThat(postInDB.getName()).isEqualTo(post2.getName());
        assertThat(postInDB.getDescription()).isEqualTo(post2.getDescription());
        assertThat(postInDB.getCreated().getSecond()).isEqualTo(post2.getCreated().getSecond());
        assertThat(postInDB.getCity()).isEqualTo(post2.getCity());
        assertThat(postInDB.isVisible()).isEqualTo(post2.isVisible());
    }
}