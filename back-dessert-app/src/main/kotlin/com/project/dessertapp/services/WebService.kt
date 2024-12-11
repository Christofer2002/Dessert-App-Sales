package com.project.dessertapp.services

import com.project.dessertapp.dto.*
import com.project.dessertapp.entities.product.Taste
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

//------------Start PRODUCT CONTROLLER GET/POST/PUT/DELETE------------
@RestController
@RequestMapping("\${url.products}")
class ProductController(
    @Qualifier("abstractProductService") private val productService: ProductService,
) {

    @GetMapping()
    @ResponseBody
    fun findAllProducts(): List<ProductDTO>? {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for listing All Products")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productService.findAllProduct()
    }

    @GetMapping("/pd")
    @ResponseBody
    fun findProductById(@RequestParam id: Long): ProductDTO? {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for listing ProductById -> ID: $id")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productService.findProductById(id)
    }

    @GetMapping("/category")
    @ResponseBody
    fun findProductsByCategory(@RequestParam(required = false) id: Long): List<ProductDTO>? {
        return productService.findProductsByCategoryId(id)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun createProduct(@RequestBody product: ProductDTO): ResponseEntity<ProductDTOResult> {
        val createdProduct = productService.createProduct(product)
        return if (createdProduct != null) {
            try {
                val appIn = ApplicationInsightsLogger()
                appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for creating a new Product -> Details: $createdProduct")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            ResponseEntity.status(HttpStatus.CREATED).body(createdProduct) // 201 Created
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null) // 500 Internal Server Error
        }
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun updateProduct(@RequestBody product: ProductDTO) : ProductDTOResult? {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for update a Product -> Product ID: ${product.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productService.updateProduct(product)
    }

    @DeleteMapping()
    @ResponseBody
    fun deleteProductById(@RequestParam id:Long): Boolean {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request to delete a Product -> Product ID: $id")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productService.deleteProductById(id)
    }

}
//------------END PRODUCT CONTROLLER GET/POST/PUT/DELETE------------

//------------Start CATEGORY CONTROLLER GET/POST/PUT/DELETE------------
@RestController
@RequestMapping("\${url.categories}")
class CategoryController(
    @Qualifier("abstractCategoryService") private val categoryService: CategoryService,
) {
    @GetMapping()
    @ResponseBody
    fun findAllCategories(): List<CategoryDTO>? {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for listing All Categories")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categoryService.findAllCategory()
    }

    @GetMapping("/ct")
    @ResponseBody
    fun findCategoryById(@RequestParam id: Long): CategoryDTO? {
        return categoryService.findCategoryById(id)
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun createCategory(@RequestBody category: CategoryDTO) : CategoryDTOResult? {
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: Received Request for create a Category -> Details: $category")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categoryService.createCategory(category)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun updateCategory(@RequestBody category: CategoryDTO) : CategoryDTOResult? {
        return categoryService.updateCategory(category)
    }

    @DeleteMapping()
    @ResponseBody
    fun deleteCategoryById(@RequestParam(required = false) id:Long) {
        categoryService.deleteCategoryById(id)
    }
}
//------------END CATEGORY CONTROLLER GET/POST/PUT/DELETE------------

//------------Start TASTE CONTROLLER GET/POST/PUT/DELETE------------
@RestController
@RequestMapping("\${url.tastes}")
class TasteController(
    @Qualifier("abstractTasteService") private val tasteService: TasteService,
) {

    @GetMapping
    @ResponseBody
    fun findTastes(@RequestParam(required = false) id: Long?): List<TasteDTO>? {
        return if (id != null) {
            listOfNotNull(tasteService.findTasteById(id))
        } else {
            try {
                val appIn: ApplicationInsightsLogger = ApplicationInsightsLogger()
                appIn.logEvent("Backend: FME - New Get app tastes request")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            tasteService.findAllTaste()
        }
    }

    @PostMapping( consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun saveTasteUser(@RequestParam userId: String, @RequestBody taste: List<Taste>) : List<UserTastesDTO>? {
        return tasteService.saveTastes(userId, taste)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun updateTaste(@RequestBody taste: TasteDTO) : TasteDTOResult? {
        return tasteService.updateTaste(taste)
    }

    @DeleteMapping()
    @ResponseBody
    fun deleteTasteById(@RequestParam(required = false) id:Long) {
        tasteService.deleteTasteById(id)
    }
}
//------------End TASTE CONTROLLER GET/POST/PUT/DELETE------------

//------------Start EVENTPRODUCT CONTROLLER GET/POST/PUT/DELETE------------
@RestController
@RequestMapping("\${url.eventProducts}")
class EventProductController(private val eventProductService: EventProductService) {
    @GetMapping("/event")
    @ResponseBody
    fun getProductsByEventId(@RequestParam id: Long): List<ProductDTO> {
        return eventProductService.findProductsByEventId(id)
    }

    @GetMapping()
    @ResponseBody
    fun getAllEventProduct(): List<EventProductDTO> {
        return eventProductService.findAllEventProducts()
    }
}

//------------End EVENTPRODUCT CONTROLLER GET/POST/PUT/DELETE------------

//------------Start DISCOUNT CONTROLLER GET------------
@RestController
@RequestMapping("\${url.discounts}")
class DiscountController(
    @Qualifier("abstractDiscountService") private val discountService: DiscountService
) {
    //------------Start Discount GET------------
    @GetMapping("/{id}")
    @ResponseBody
    fun findDiscountById(@PathVariable id: Long) = discountService.findDiscountById(id)

    @GetMapping("code")
    @ResponseBody
    fun findDiscountByCode(@RequestParam code: String) = discountService.findDiscountByCode(code)
}

//------------End TASTE CONTROLLER GET------------

//------------Start EVENT CONTROLLER GET------------
@RestController
@RequestMapping("\${url.events}")
class EventController(
    @Qualifier("abstractEventService") private val eventService: EventService
) {
    //------------Start Event GET------------
    @GetMapping
    @ResponseBody
    fun findAllEvents(): List<EventDTO>? {
        return eventService.findAllEvents()
    }

    @GetMapping("/ev")
    @ResponseBody
    fun findEventById(@RequestParam id: Long): EventDTO? {
        return eventService.findEventById(id)
    }
}

//------------End EVENT CONTROLLER GET------------

//------------Start ORDER CONTROLLER GET------------

@RestController
@RequestMapping("\${url.orders}")
class OrderController(
    @Qualifier("abstractOrderService") private val orderService: OrderService
) {

    // Endpoint to add an order
    @PostMapping("/createWithDetails")
    fun addOrderWithDetails(
        @RequestBody orderWithDetailsDTO: OrderWithDetailsDTO // A DTO that includes Order and OrderDetails
    ): ResponseEntity<OrderDTO> {
        val createdOrder = orderService.createOrderWithDetails(orderWithDetailsDTO.order, orderWithDetailsDTO.orderDetails)
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: New order placed -> Order details: $createdOrder")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder)
    }


    @GetMapping
    @ResponseBody
    fun findAllOrder(): List<OrderDTO>? {
        return orderService.findAllOrders()
    }

    @GetMapping("/or")
    @ResponseBody
    fun findOrderById(@RequestParam id: Long): OrderDTO? {
        return orderService.findOrderById(id)
    }

    @GetMapping("/details")
    @ResponseBody
    fun findOrderDetailsByOrderId(@RequestParam id: Long): List<OrderDetailsDTO>? {
        return orderService.findOrderDetailsByOrderId(id)
    }

    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun updateOrder(@RequestParam orderId: Long, @RequestParam statusId: Long) : Boolean {
        return orderService.updateOrder(orderId, statusId)
    }
}

@RestController
@RequestMapping("\${url.user.signup}")
class SignupController(
    @Qualifier("abstractUserService") private val userService: UserService
){
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun createUser(@RequestBody user: UserDTO) : UserDTOResult? {
        println("Received UserDTO: $user")
        try {
            val appIn = ApplicationInsightsLogger()
            appIn.logEvent("LOG-DessertSalesApp-Backend: New user registered -> User details: $user")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return userService.createUser(user)
    }
}
