package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class PostDBStore {
    private final static String FIND_ALL = "SELECT * FROM post";
    private final static String ADD = "INSERT INTO post(name, description, created, city_id, visible) "
            + "VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE post SET name = ?, description = ?, created = ?, "
            + "city_id = ?, visible = ? WHERE id = ?";
    private final static String FIND_BY_ID = "SELECT * FROM post WHERE id = ?";

    private final BasicDataSource pool;
    private final static Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPost(it));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findAll()", e);
        }
        return posts;
    }

    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(ADD,
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
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Post)", e);
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.setInt(4, post.getCity().getId());
            ps.setBoolean(5, post.isVisible());
            ps.setInt(6, post.getId());
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in method .update(Post)", e);
        }
    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createPost(it);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findById(int)", e);
        }
        return null;
    }

    private Post createPost(ResultSet rs) throws SQLException {
        Post post = new Post(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getTimestamp("created").toLocalDateTime());
        post.setCity(new City(rs.getInt("city_id"), ""));
        post.setVisible(rs.getBoolean("visible"));
        return post;
    }
}
