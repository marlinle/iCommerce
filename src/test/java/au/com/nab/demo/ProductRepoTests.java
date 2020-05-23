package au.com.nab.demo;

import au.com.nab.demo.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductRepoTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private void prepareData() throws Exception {
        mockMvc.perform(post("/products")
                .content("{\"name\": \"Fossil Q\", \"price\":\"100\", \"brand\": \"fossil\", \"color\":\"red\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/products")
                .content("{\"name\": \"Fossil Q\", \"price\":\"100\", \"brand\": \"fossil\", \"color\":\"blue\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/products")
                .content("{\"name\": \"Skagen Sport\", \"price\":\"120\", \"brand\": \"skagen\", \"color\":\"red\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/products")
                .content("{\"name\": \"Skagen Sport\", \"price\":\"120\", \"brand\": \"skagen\", \"color\":\"blue\"}"))
                .andExpect(status().isCreated());
    }

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        productRepository.deleteAll();
        prepareData();
    }

    @Test
    public void shouldCreateEntityAndContextLoad() throws Exception {
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(4));
    }

    @Test
    public void shouldFindByName() throws Exception {
        mockMvc.perform(get("/products/search/findByName").queryParam("name", "Fossil Q"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(2)))
                .andExpect(jsonPath("$._embedded.products[0].name").value("Fossil Q"))
                .andExpect(jsonPath("$._embedded.products[1].name").value("Fossil Q"));
    }

    @Test
    public void shouldFindByBrand() throws Exception {
        mockMvc.perform(get("/products/search/findByBrand").queryParam("brand", "fossil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(2)))
                .andExpect(jsonPath("$._embedded.products[0].brand").value("fossil"))
                .andExpect(jsonPath("$._embedded.products[1].brand").value("fossil"));
    }

    @Test
    public void shouldFindByColor() throws Exception {
        mockMvc.perform(get("/products/search/findByColor").queryParam("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(2)))
                .andExpect(jsonPath("$._embedded.products[0].color").value("red"))
                .andExpect(jsonPath("$._embedded.products[1].color").value("red"));
    }

    @Test
    public void shouldFindByPriceBetween() throws Exception {
        mockMvc.perform(get("/products/search/findByPriceBetween")
                .queryParam("priceLow", "100").queryParam("priceHigh", "110"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.products", hasSize(2)))
                .andExpect(jsonPath("$._embedded.products[0].name").value("Fossil Q"))
                .andExpect(jsonPath("$._embedded.products[1].name").value("Fossil Q"));
    }

//    @Test
    public void shouldAssociation() throws Exception {
        // TODO : add association test via text/uri-list
    }
}
