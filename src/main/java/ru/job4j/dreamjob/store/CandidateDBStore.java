package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {
    private final static String FIND_ALL = "SELECT * FROM candidate";
    private final static String ADD = "INSERT INTO candidate(name, description, created, photo) "
            + "VALUES (?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE candidate SET name = ?, description = ?,"
            + " created = ?, photo = ? WHERE id = ?";
    private final static String FIND_BY_ID = "SELECT * FROM candidate WHERE id = ?";
    private final static Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(createCandidate(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findAll()", e);
        }
        return candidates;
    }

    public void add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(ADD, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.setBytes(4, candidate.getPhoto());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Candidate)", e);
        }
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDesc());
            ps.setTimestamp(3, Timestamp.valueOf(candidate.getCreated()));
            ps.setBytes(4, candidate.getPhoto());
            ps.setInt(5, candidate.getId());
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in method .add(Candidate)", e);
        }
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createCandidate(rs);
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in method .findById(int)", e);
        }
        return null;
    }

    private Candidate createCandidate(ResultSet rs) throws SQLException {
        Candidate candidate = new Candidate(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getTimestamp("created").toLocalDateTime());
        candidate.setPhoto(rs.getBytes("photo"));
        return candidate;
    }
}
