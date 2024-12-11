package com.project.dessertapp

import com.project.dessertapp.entities.event.Event
import com.project.dessertapp.repository.category.CategoryRepository
import com.project.dessertapp.entities.product.Category
import com.project.dessertapp.entities.product.Product
import com.project.dessertapp.repository.event.EventRepository
import com.project.dessertapp.repository.product.ProductRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertTrue

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(
    statements = [
        "DELETE FROM public.events_products",
        "DELETE FROM public.products",
        "DELETE FROM public.events",
        "DELETE FROM public.categories"  // Clean up the join table
    ],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = ["/import-categories.sql", "/import-products.sql", "/import-events.sql", "/import-events_products.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class LoadInitData(
    @Autowired
    val categoryRepository: CategoryRepository,
    @Autowired
    val productRepository: ProductRepository,  // Inject ProductRepository
    @Autowired
    val eventRepository: EventRepository
) {

    // Category tests
    @Test
    fun testCategoryFindAll() {
        val categoryList: List<Category> = categoryRepository.findAll()
        assertEquals(3, categoryList.size, "The number of categories should be 3")
    }

    @Test
    fun testCategoryFindById() {
        val categoryOptional = categoryRepository.findById(1)
        Assertions.assertTrue(categoryOptional.isPresent, "Category with ID 1 should exist")
        val category = categoryOptional.get()
        category.id?.let { assertEquals(1, it.toInt(), "Category ID should be 1") }
    }

    // Tests Product
    @Test
    fun testProductFindById() {
        val productOptional = productRepository.findById(1)
        Assertions.assertTrue(productOptional.isPresent, "Product with ID 1 should exist")
        val product = productOptional.get()
        product.id?.let { assertEquals(1, it.toInt(), "Product ID should be 1") }
        assertEquals("Apple Pie", product.name, "Product name should be 'Apple Pie'")
    }

    @Test
    fun testProductFindAll() {
        val productList: List<Product> = productRepository.findAll()
        productList.forEach { product ->
            println("Product ID: ${product.id}, Name: ${product.name}, Category: ${product.categoryId}")
        }
        assertFalse(productList.isEmpty(), "Product list should not be empty")
        assertEquals(5, productList.size, "Product list size should be 5")
    }

    // Event tests
    @Test
    fun testFindAllEvents() {
        val eventList: List<Event> = eventRepository.findAll()
        assertFalse(eventList.isEmpty(), "Event list should not be empty")
        assertEquals(3, eventList.size, "There should be 3 events in the list")
    }

    @Test
    fun testFindEventById() {
        val eventOptional = eventRepository.findById(1L)
        assertTrue(eventOptional.isPresent, "Event with ID 1 should exist")
        val event = eventOptional.get()
        assertEquals(1L, event.id)
        assertEquals("Birthday Party", event.name)
        assertEquals("https://example.com/birthday.jpg", event.image)
        assertEquals("Celebrate a birthday with delicious desserts", event.description)
    }

    // Test of the relationship between Event and Product
    @Test
    @Transactional
    fun testEventProductRelationship() {
        val eventOptional = eventRepository.findById(1L)
        assertTrue(eventOptional.isPresent, "Event with ID 1 should exist")
        val event = eventOptional.get()

        val products = event.products
        assertFalse(products.isEmpty(), "The event should have products associated")
        assertEquals(2, products.size, "The event should have 2 products associated")

        products.forEach { product ->
            println("Event ${event.name} has product: ${product.name}")
        }
    }
}
