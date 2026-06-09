package fr.sidranie.grocery.product;

import static fr.sidranie.grocery.product.ProductResultMatcher.matchesProduct;
import static fr.sidranie.grocery.product.ProductResultMatcher.matchesProducts;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.product.dto.ProductDto;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private Products products;

    @MockitoBean
    private ProductService productService;

    private static List<Product> testProducts;

    @BeforeAll
    static void setUp() {
        Product first = new Product(1L,
                                    new Identifier("tomato"),
                                    new Slug("tomato"),
                                    new Price(1.2f));
        Product second = new Product(1L,
                                     new Identifier("Apple pie"),
                                     new Slug("apple-pie"),
                                     new Price(1.2f));
        testProducts = List.of(first, second);
    }

    @Test
    @WithMockUser(username = "user")
    void testGetAll() throws Exception {
        when(products.findAll()).thenReturn(testProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpectAll(matchesProducts("$.data", testProducts));
    }

    @Test
    void testGetAll_anonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void testGetBySlug() throws Exception {
        Product expected = testProducts.getFirst();

        when(products.findBySlug(expected.getSlug())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/products/{slug}", expected.getSlug().getValue()))
                .andExpect(status().isOk())
                .andExpectAll(matchesProduct("$.data", expected));
    }

    @Test
    void testGetBySlug_anonymous() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/slug"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void testGetBySlug_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/slug"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testCreateProduct() throws Exception {
        String name = "product";
        Identifier productName = new Identifier(name);
        Slug slug = new Slug(name);
        Price productPrice = new Price(1.f);

        ProductDto inputDto = new ProductDto(
                productName,
                productPrice
        );
        Product input = new Product();
        input.setName(productName);
        input.setPrice(productPrice);
        Product expected = new Product(1L, productName, slug, productPrice);

        when(productService.save(input)).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpectAll(matchesProduct("$.data", expected));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testCreateProduct_duplicateName() throws Exception {
        String name = "product";
        Identifier productName = new Identifier(name);
        Price productPrice = new Price(1.f);

        ProductDto inputDto = new ProductDto(
                productName,
                productPrice
        );
        Product input = new Product();
        input.setName(productName);
        input.setPrice(productPrice);

        String expectedMessage = String.format(ProductService.MESSAGE_NAME_ALREADY_EXISTS, productName);

        when(productService.save(input)).thenThrow(new IllegalArgumentException(expectedMessage));

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testCreateProduct_duplicateSlug() throws Exception {
        String name = "product";
        Identifier productName = new Identifier(name);
        Price productPrice = new Price(1.f);

        ProductDto inputDto = new ProductDto(
                productName,
                productPrice
        );
        Product input = new Product();
        input.setName(productName);
        input.setPrice(productPrice);

        String expectedMessage = String.format(ProductService.MESSAGE_SLUG_ALREADY_EXISTS, productName);

        when(productService.save(input)).thenThrow(new IllegalArgumentException(expectedMessage));

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER", "ADMIN"})
    void testCreateProduct_invalidPrice() throws Exception {
        String name = "product";
        Identifier productName = new Identifier(name);
        Price productPrice = new Price(-1.f);

        ProductDto inputDto = new ProductDto(
                productName,
                productPrice
        );
        Product input = new Product();
        input.setName(productName);
        input.setPrice(productPrice);

        when(productService.save(input))
                .thenThrow(new IllegalArgumentException(ProductService.MESSAGE_INVALID_PRICE));

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ProductService.MESSAGE_INVALID_PRICE));
    }

    @Test
    void testCreateProduct_anonymous() throws Exception {
        ProductDto inputDto = new ProductDto(
                new Identifier("name"),
                new Price(1.f)
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user")
    void testCreateProduct_roleUser() throws Exception {
        ProductDto inputDto = new ProductDto(
                new Identifier("name"),
                new Price(1.f)
        );
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isForbidden());
    }
}
