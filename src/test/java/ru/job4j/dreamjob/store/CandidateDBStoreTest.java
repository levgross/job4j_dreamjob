package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CandidateDBStoreTest {
    @Test
    public void whenCreateCandidate() {
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
}