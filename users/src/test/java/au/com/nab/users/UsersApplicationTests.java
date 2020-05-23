package au.com.nab.users;

import au.com.nab.users.repo.UserRepository;
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
    private UserRepository userRepository;

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void shouldContextLoad() throws Exception {
    }

    @Test
    public void shouldCreateEntity() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\"fbId\": \"1234567890\", \"firstName\":\"Marlin\", \"lastName\": \"Le\", \"gender\":\"MALE\", \"age\":37}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        mockMvc.perform(post("/users")
                .content("{\"fbId\": \"1234567890\", \"firstName\":\"Marlin\", \"lastName\": \"Le\", \"gender\":\"MALE\", \"age\":37}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.users[0].firstName").value("Marlin"));
    }
}