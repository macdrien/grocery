package fr.sidranie.grocery.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import fr.sidranie.grocery.data.identifier.Identifier;
import fr.sidranie.grocery.data.price.Price;
import fr.sidranie.grocery.data.slug.Slug;
import fr.sidranie.grocery.exception.NotFoundException;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @MockitoBean
    private Products products;

    @Test
    void testSave() {
        String productName = "Test product";
        Identifier identifier = new Identifier(productName);
        Slug slug = Slug.fromString(productName);
        Price price = new Price(1f);

        Product input = new Product();
        input.setName(identifier);
        // Another slug to test that the method correctly update it
        input.setSlug(Slug.fromString("Another test product"));
        input.setPrice(price);

        Product saveInput = new Product(null, identifier, slug, price);
        Product expected = new Product(1L, identifier, slug, price);

        when(products.existsByName(identifier)).thenReturn(false);
        when(products.existsBySlug(slug)).thenReturn(false);
        when(products.save(saveInput)).thenReturn(expected);

        Product result = service.save(input);

        assertEquals(expected, result);
    }

    @Test
    void testSave_withDuplicatedName() {
        String productName = "Test product";
        Identifier identifier = new Identifier(productName);

        Product input = new Product();
        input.setName(identifier);

        when(products.existsByName(identifier)).thenReturn(true);

        try {
            service.save(input);
        } catch (IllegalArgumentException exception) {
            assertEquals(ProductService.MESSAGE_NAME_ALREADY_EXISTS.formatted(productName),
                         exception.getMessage());
        }
    }

    @Test
    void testSave_withDuplicatedSlug() {
        String productName = "Test product";
        Identifier identifier = new Identifier(productName);
        Slug slug = Slug.fromString(productName);

        Product input = new Product();
        input.setName(identifier);

        when(products.existsByName(identifier)).thenReturn(false);
        when(products.existsBySlug(slug)).thenReturn(true);

        try {
            service.save(input);
        } catch (IllegalArgumentException exception) {
            assertEquals(ProductService.MESSAGE_SLUG_ALREADY_EXISTS.formatted(slug.getValue()),
                         exception.getMessage());
        }
    }

    @Test
    void testSave_invalidPrice() {
        String productName = "Test product";
        Identifier identifier = new Identifier(productName);
        Slug slug = Slug.fromString(productName);
        Price price = new Price(-1f);

        Product input = new Product();
        input.setName(identifier);
        input.setSlug(slug);
        input.setPrice(price);

        when(products.existsByName(identifier)).thenReturn(false);
        when(products.existsBySlug(slug)).thenReturn(false);

        try {
            service.save(input);
        } catch (IllegalArgumentException exception) {
            assertEquals(ProductService.MESSAGE_INVALID_PRICE, exception.getMessage());
        }
    }

    @Test
    void testUpdateProduct_fullUpdate() throws NotFoundException {
        Long id = 1L;
        String oldName = "Old name";
        String newName = "New name";
        Identifier newIdentifier = new Identifier(newName);
        Slug newSlug = Slug.fromString(newName);

        Product baseProduct = new Product(1L,
                                          new Identifier(oldName),
                                          Slug.fromString(oldName),
                                          new Price(1f));
        Product updates = new Product(null,
                                      newIdentifier,
                                      newSlug,
                                      new Price(1.5f));
        Product expected = new Product(1L,
                                       newIdentifier,
                                       newSlug,
                                       new Price(1.5f));

        when(products.findById(id)).thenReturn(Optional.of(baseProduct));
        when(products.existsByName(newIdentifier)).thenReturn(false);
        when(products.existsBySlug(newSlug)).thenReturn(false);
        when(products.save(expected)).thenReturn(expected);

        Product result = service.updateProduct(id, updates);

        assertEquals(expected, result);
    }

    @Test
    void testUpdateProduct_noBaseProduct() {
        Long id = 1L;
        Product updates = new Product();

        when(products.findById(id)).thenReturn(Optional.empty());

        try {
            service.updateProduct(id, updates);
        } catch (NotFoundException e) {
            assertEquals(ProductService.MESSAGE_NO_PRODUCT_WITH_ID_FOUND.formatted(id),
                         e.getMessage());
        }
    }

    @Test
    void testUpdateProduct_duplicateName() throws NotFoundException {
        Long id = 1L;
        String oldName = "Old name";
        String newName = "New name";
        Identifier newIdentifier = new Identifier(newName);

        Product baseProduct = new Product(1L,
                                          new Identifier(oldName),
                                          Slug.fromString(oldName),
                                          new Price(1f));
        Product updates = new Product(null,
                                      newIdentifier,
                                      Slug.fromString(newName),
                                      new Price(1.5f));

        when(products.findById(id)).thenReturn(Optional.of(baseProduct));
        when(products.existsByName(newIdentifier)).thenReturn(true);

        try {
            service.updateProduct(id, updates);
        } catch (IllegalArgumentException e) {
            assertEquals(ProductService.MESSAGE_NAME_ALREADY_EXISTS.formatted(newName),
                         e.getMessage());
        }
    }

    @Test
    void testUpdateProduct_duplicateSlug() throws NotFoundException {
        Long id = 1L;
        String oldName = "Old name";
        String newName = "New name";
        Identifier newIdentifier = new Identifier(newName);
        Slug newSlug = Slug.fromString(newName);

        Product baseProduct = new Product(1L,
                                          new Identifier(oldName),
                                          Slug.fromString(oldName),
                                          new Price(1f));
        Product updates = new Product(null,
                                      newIdentifier,
                                      newSlug,
                                      new Price(1.5f));

        when(products.findById(id)).thenReturn(Optional.of(baseProduct));
        when(products.existsByName(newIdentifier)).thenReturn(false);
        when(products.existsBySlug(newSlug)).thenReturn(true);

        try {
            service.updateProduct(id, updates);
        } catch (IllegalArgumentException e) {
            assertEquals(ProductService.MESSAGE_SLUG_ALREADY_EXISTS.formatted(newSlug.getValue()),
                         e.getMessage());
        }
    }

    @Test
    void testUpdateProduct_invalidPrice() throws NotFoundException {
        Long id = 1L;
        String oldName = "Old name";
        String newName = "New name";
        Identifier newIdentifier = new Identifier(newName);
        Slug newSlug = Slug.fromString(newName);
        Price newPrice = new Price(-1.5f);

        Product baseProduct = new Product(1L,
                                          new Identifier(oldName),
                                          Slug.fromString(oldName),
                                          new Price(1f));
        Product updates = new Product(null,
                                      newIdentifier,
                                      newSlug,
                                      newPrice);

        when(products.findById(id)).thenReturn(Optional.of(baseProduct));
        when(products.existsByName(newIdentifier)).thenReturn(false);
        when(products.existsBySlug(newSlug)).thenReturn(false);

        try {
            service.updateProduct(id, updates);
        } catch (IllegalArgumentException e) {
            assertEquals(ProductService.MESSAGE_INVALID_PRICE.formatted(newPrice.getValue()),
                         e.getMessage());
        }
    }
}
