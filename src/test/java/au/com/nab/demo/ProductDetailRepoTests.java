package au.com.nab.demo;

import au.com.nab.demo.repo.ProductDetailRepository;
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
class ProductDetailRepoTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        productDetailRepository.deleteAll();
    }

    @Test
    public void shouldContextLoad() throws Exception {
    }

    @Test
    public void shouldCreateEntity() throws Exception {
        mockMvc.perform(post("/productDetails")
                .content("{\"sku\":\"FS0001\", \"description\":\"Fossil Hybrid Smartwatch ...\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        mockMvc.perform(post("/productDetails")
                .content("{\"sku\":\"FS0001\", \"description\":\"Fossil Hybrid Smartwatch ...\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/productDetails")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productDetails[0].sku").value("FS0001"));
    }
}
