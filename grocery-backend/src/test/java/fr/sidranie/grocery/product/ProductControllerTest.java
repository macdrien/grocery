package fr.sidranie.grocery.product;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.data.slug.Slug;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Products products;

    @Test
    @WithMockUser(username = "user")
    public void testGetAll() throws Exception {
        Product first = new Product(1L,
                                    new Identifier("tomato"),
                                    new Slug("tomato"),
                                    new Price(1.2f));
        Product second = new Product(1L,
                                     new Identifier("Apple pie"),
                                     new Slug("apple-pie"),
                                     new Price(1.2f));
        List<Product> expectedProducts = List.of(first, second);

        when(products.findAll()).thenReturn(expectedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is(first.getName().getValue())))
                .andExpect(jsonPath("$.data[1].name", is(second.getName().getValue())));
    }

    @Test
    public void testGetAll_anonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isUnauthorized());
    }
}
