package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PostDBStoreTest {
    @Test
    public void whenCreatePost() {
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
}