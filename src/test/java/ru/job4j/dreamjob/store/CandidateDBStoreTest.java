package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CandidateDBStoreTest {
    @Test
    public void whenAddCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate.setPhoto(new byte[1028]);
        store.add(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertThat(candidateInDB.getName()).isEqualTo(candidate.getName());
        assertThat(candidateInDB.getDesc()).isEqualTo(candidate.getDesc());
        assertThat(candidateInDB.getCreated()).isEqualTo(candidate.getCreated());
        assertThat(candidateInDB.getPhoto()).isEqualTo(candidate.getPhoto());
    }

    @Test
    public void whenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate1.setPhoto(new byte[1028]);
        store.add(candidate1);
        Candidate candidate2 = new Candidate(0, "Sam Johnson", "Description2", LocalDateTime.now());
        candidate2.setPhoto(new byte[514]);
        Candidate candidateInDB = store.findById(0);
        assertThat(candidateInDB.getName()).isEqualTo(candidate2.getName());
        assertThat(candidateInDB.getDesc()).isEqualTo(candidate2.getDesc());
        assertThat(candidateInDB.getCreated()).isEqualTo(candidate2.getCreated());
        assertThat(candidateInDB.getPhoto()).isEqualTo(candidate2.getPhoto());
    }

    @Test
    public void whenFindAllCandidates() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate1.setPhoto(new byte[1028]);
        store.add(candidate1);
        Candidate candidate2 = new Candidate(1, "Sam Johnson", "Description2", LocalDateTime.now());
        candidate2.setPhoto(new byte[514]);
        store.add(candidate2);
        assertThat(store.findAll()).containsExactly(candidate1, candidate2);
    }
}