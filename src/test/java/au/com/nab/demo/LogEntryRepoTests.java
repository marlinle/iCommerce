package au.com.nab.demo;

import au.com.nab.demo.repo.LogEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LogEntryRepoTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LogEntryRepository logEntryRepository;

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        logEntryRepository.deleteAll();
    }

    @Test
    public void shouldContextLoad() throws Exception {
    }

    @Test
    public void shouldCreateEntity() throws Exception {
        mockMvc.perform(post("/logEntries")
                .content("{\"timestamp\": \"2017-12-15 10:00\", \"severity\":\"INFO\", \"service\": \"product\", \"action\":\"findAll\", \"result\":\"200 OK\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        mockMvc.perform(post("/logEntries")
                .content("{\"timestamp\": \"2017-12-15 10:00\", \"severity\":\"INFO\", \"service\": \"product\", \"action\":\"findAll\", \"result\":\"200 OK\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/logEntries")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.logEntries[0].service").value("product"));
    }
}
