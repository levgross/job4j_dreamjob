package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CandidateDBStoreTest {

    CandidateDBStore store = new CandidateDBStore(new Main().loadPool());

    @Test
    public void whenAddCandidate() {
        Candidate candidate = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate.setPhoto(new byte[1028]);
        store.add(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertThat(candidateInDB.getName()).isEqualTo(candidate.getName());
        assertThat(candidateInDB.getDesc()).isEqualTo(candidate.getDesc());
        assertThat(candidateInDB.getCreated().getSecond()).isEqualTo(candidate.getCreated().getSecond());
        assertThat(candidateInDB.getPhoto()).isEqualTo(candidate.getPhoto());
    }

    @Test
    public void whenUpdateCandidate() {
        Candidate candidate1 = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate1.setPhoto(new byte[1028]);
        store.add(candidate1);
        Candidate candidate2 = new Candidate(candidate1.getId(), "Sam Johnson", "Description2", LocalDateTime.now());
        candidate2.setPhoto(new byte[514]);
        store.update(candidate2);
        Candidate candidateInDB = store.findById(candidate1.getId());
        assertThat(candidateInDB.getName()).isEqualTo(candidate2.getName());
        assertThat(candidateInDB.getDesc()).isEqualTo(candidate2.getDesc());
        assertThat(candidateInDB.getCreated().getSecond()).isEqualTo(candidate2.getCreated().getSecond());
        assertThat(candidateInDB.getPhoto()).isEqualTo(candidate2.getPhoto());
    }

    @Test
    public void whenFindAllCandidates() {
        Candidate candidate1 = new Candidate(0, "John Smith", "Description", LocalDateTime.now());
        candidate1.setPhoto(new byte[1028]);
        store.add(candidate1);
        Candidate candidate2 = new Candidate(1, "Sam Johnson", "Description2", LocalDateTime.now());
        candidate2.setPhoto(new byte[514]);
        store.add(candidate2);
        assertThat(store.findAll()).contains(candidate1, candidate2);
    }
}