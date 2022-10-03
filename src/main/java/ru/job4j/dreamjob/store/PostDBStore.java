package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDBStore {
    private final BasicDataSource pool;
    private final CityService cityService;

    public PostDBStore(BasicDataSource pool, CityService cityService) {
        this.pool = pool;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Post post = new Post(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
                    post.setCity(cityService.findById(it.getInt("city_id")));
                    post.setVisible(it.getBoolean("visible"));
                    posts.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "INSERT INTO post(name, description, created, city_id, visible) VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.setInt(4, post.getCity().getId());
            ps.setBoolean(5, post.isVisible());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                "UPDATE post SET name = ?, description = ?, created = ?, city_id = ?, visible = ? WHERE id = ?")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.setInt(4, post.getCity().getId());
            ps.setBoolean(5, post.isVisible());
            ps.setInt(6, post.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    Post post = new Post(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime());
                    post.setCity(cityService.findById(it.getInt("city_id")));
                    post.setVisible(Boolean.parseBoolean("visible"));
                    return post;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
