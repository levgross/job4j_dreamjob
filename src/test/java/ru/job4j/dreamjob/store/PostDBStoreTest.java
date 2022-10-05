package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PostDBStoreTest {
    @Test
    public void whenAddPost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post.setCity(new City(0, "NYC"));
        post.setVisible(true);
        store.add(post);
        Post postInDB = store.findById(post.getId());
        assertThat(postInDB.getName()).isEqualTo(post.getName());
        assertThat(postInDB.getDescription()).isEqualTo(post.getDescription());
        assertThat(postInDB.getCreated()).isEqualTo(post.getCreated());
        assertThat(postInDB.getCity()).isEqualTo(post.getCity());
        assertThat(postInDB.isVisible()).isEqualTo(post.isVisible());
    }

    @Test
    public void whenFindAllPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post1.setCity(new City(0, "NYC"));
        post1.setVisible(true);
        store.add(post1);
        Post post2 = new Post(1, "Java Job Dev", "Description Dev", LocalDateTime.now());
        post2.setCity(new City(1, "Moscow"));
        post2.setVisible(false);
        store.add(post2);
        assertThat(store.findAll()).containsExactly(post1, post2);
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post1 = new Post(0, "Java Job", "Description", LocalDateTime.now());
        post1.setCity(new City(0, "NYC"));
        post1.setVisible(true);
        store.add(post1);
        Post post2 = new Post(0, "Java Job Dev", "Description Dev", LocalDateTime.now());
        post2.setCity(new City(1, "Moscow"));
        post2.setVisible(false);
        store.update(post2);
        Post postInDB = store.findById(0);
        assertThat(postInDB.getName()).isEqualTo(post2.getName());
        assertThat(postInDB.getDescription()).isEqualTo(post2.getDescription());
        assertThat(postInDB.getCreated()).isEqualTo(post2.getCreated());
        assertThat(postInDB.getCity()).isEqualTo(post2.getCity());
        assertThat(postInDB.isVisible()).isEqualTo(post2.isVisible());
    }
}