package com.project.dessertapp.services

import com.project.dessertapp.dto.*
import com.project.dessertapp.entities.core.Role
import com.project.dessertapp.entities.core.User
import com.project.dessertapp.entities.event.Event
import com.project.dessertapp.entities.product.Category
import com.project.dessertapp.entities.product.Discount
import com.project.dessertapp.entities.product.Product
import com.project.dessertapp.entities.product.Taste
import com.project.dessertapp.repository.category.CategoryRepository
import com.project.dessertapp.repository.event.EventRepository
import com.project.dessertapp.repository.order.DiscountRepository
import com.project.dessertapp.repository.product.ProductRepository
import com.project.dessertapp.dto.EventProductDTO
import com.project.dessertapp.entities.associations.UserTastes
import com.project.dessertapp.entities.order.Order
import com.project.dessertapp.entities.order.Status
import com.project.dessertapp.repository.event.EventProductRepository
import com.project.dessertapp.repository.order.OrderDetailRepository
import com.project.dessertapp.repository.order.OrderRepository
import com.project.dessertapp.repository.order.StatusRepository
import com.project.dessertapp.repository.product.TasteRepository
import com.project.dessertapp.repository.user.RoleRepository
import com.project.dessertapp.repository.user.UserRepository
import com.project.dessertapp.repository.user.UserTasteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ProductService {

    fun findAllProduct(): List<ProductDTO>?
    fun findProductById(id: Long): ProductDTO?
    fun findProductsByCategoryId(categoryId: Long): List<ProductDTO>?
    fun createProduct(product: ProductDTO): ProductDTOResult?
    fun updateProduct(product: ProductDTO): ProductDTOResult?
    fun deleteProductById(id: Long): Boolean
}

@Service
class AbstractProductService(
    @Autowired
    val productRepository: ProductRepository,

    @Autowired
    val productMapper: AllMapper,
    ): ProductService {
    override fun findAllProduct(): List<ProductDTO>? {
        return productMapper.productListToProductDTOList(
            productRepository.findAll()
        )
    }

    override fun findProductById(id: Long): ProductDTO? {
        val product: Optional<Product> = productRepository.findById(id)
        return productMapper.productToProductDTO(product.get(),)
    }

    override fun findProductsByCategoryId(categoryId: Long): List<ProductDTO>? {
        val products: List<Product> = productRepository.findByCategoryId(categoryId)
        return productMapper.productListToProductDTOList(products)
    }

    override fun createProduct(product: ProductDTO): ProductDTOResult? {
        val productCreate: Product = productMapper.productDTOToProduct(product)
        return productMapper.productToProductResult(
            productRepository.save(productCreate)
        )
    }

    override fun updateProduct(product: ProductDTO): ProductDTOResult? {
        val productUpdate: Optional<Product> = productRepository.findById(product.id!!)
        if (productUpdate.isEmpty){
            throw NoSuchElementException(String.format("The product with the id: %s not found!", product.id))
        }
        val productUpdated: Product = productUpdate.get()
        productMapper.productToProduct(product, productUpdated)
        return productMapper.productToProductResult(productRepository.save(productUpdated))
    }

    override fun deleteProductById(id: Long): Boolean {
        return if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
            true // Return true if the product is deleted
        } else {
            false // Return false if the product is not found
        }
    }

}

//--------------------Category Interface
interface CategoryService {
    fun findAllCategory(): List<CategoryDTO>?
    fun findCategoryById(id: Long): CategoryDTO?
    fun createCategory(category: CategoryDTO): CategoryDTOResult?
    fun updateCategory(category: CategoryDTO): CategoryDTOResult?
    fun deleteCategoryById(id: Long)
}

@Service
class AbstractCategoryService(
    @Autowired
    val categoryRepository: CategoryRepository,

    @Autowired
    val categoryMapper: AllMapper,
): CategoryService {
    override fun findAllCategory(): List<CategoryDTO>? {
        return categoryMapper.categoryListToCategoryDTOList(
            categoryRepository.findAll()
        )
    }

    override fun findCategoryById(id: Long): CategoryDTO? {
        val category: Optional<Category> = categoryRepository.findById(id)
        return categoryMapper.categoryToCategoryDTO(category.get(),)
    }

    override fun createCategory(category: CategoryDTO): CategoryDTOResult? {
        val categoryCreate: Category = categoryMapper.categoryDTOToCategory(category)
        return categoryMapper.categoryToCategoryResult(
            categoryRepository.save(categoryCreate)
        )
    }

    override fun updateCategory(category: CategoryDTO): CategoryDTOResult? {
        val categoryUpdate: Optional<Category> = categoryRepository.findById(category.id!!)
        if (categoryUpdate.isEmpty){
            throw NoSuchElementException(String.format("The category with the id: %s not found!", category.id))
        }
        val categoryUpdated: Category = categoryUpdate.get()
        categoryMapper.categoryToCategory(category, categoryUpdated)
        return categoryMapper.categoryToCategoryResult(categoryRepository.save(categoryUpdated))
    }

    override fun deleteCategoryById(id: Long) {
        if (!categoryRepository.findById(id).isEmpty) {
            categoryRepository.deleteById(id)
        } else {
            throw NoSuchElementException(String.format("The category with the id: %s not found!", id))
        }
    }
}


//--------------------Taste Interface
interface TasteService {
    fun findAllTaste(): List<TasteDTO>?
    fun findTasteById(id: Long): TasteDTO?
    fun saveTastes(userId: String, tastes: List<Taste>): List<UserTastesDTO>
    fun updateTaste(taste: TasteDTO): TasteDTOResult?
    fun deleteTasteById(id: Long)
}

@Service
class AbstractTasteService(
    @Autowired
    val tasteRepository: TasteRepository,

    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val userTastesRepository: UserTasteRepository,

    @Autowired
    val tasteMapper: AllMapper,
): TasteService {
    override fun findAllTaste(): List<TasteDTO>? {
        return tasteMapper.tasteListToTasteDTOList(
            tasteRepository.findAll()
        )
    }

    override fun findTasteById(id: Long): TasteDTO? {
        val taste: Optional<Taste> = tasteRepository.findById(id)
        return tasteMapper.tasteToTasteDTO(taste.get(),)
    }

    override fun saveTastes(userId: String, tastes: List<Taste>): List<UserTastesDTO> {

        val user = userRepository.findById(userId.toLong()).orElseThrow {
            IllegalArgumentException("User with ID $userId not found")
        }

        val tasteUserList = tastes.map { taste ->
            UserTastes(
                user = user,
                taste = taste
            )
        }

        val savedTasteUsers = userTastesRepository.saveAll(tasteUserList)

        return savedTasteUsers.map { savedTasteUser ->
            UserTastesDTO(
                user = tasteMapper.userToUserDTO(savedTasteUser.user), // ID del usuario
                taste = tasteMapper.tasteToTasteDTO(savedTasteUser.taste),    // ID del gusto
            )
        }
    }

    override fun updateTaste(taste: TasteDTO): TasteDTOResult? {
        val tasteUpdate: Optional<Taste> = tasteRepository.findById(taste.id!!)
        if (tasteUpdate.isEmpty){
            throw NoSuchElementException(String.format("The taste with the id: %s not found!", taste.id))
        }
        val tasteUpdated: Taste = tasteUpdate.get()
        tasteMapper.tasteToTaste(taste, tasteUpdated)
        return tasteMapper.tasteToTasteResult(tasteRepository.save(tasteUpdated))
    }

    override fun deleteTasteById(id: Long) {
        if (!tasteRepository.findById(id).isEmpty) {
            tasteRepository.deleteById(id)
        } else {
            throw NoSuchElementException(String.format("The category with the id: %s not found!", id))
        }
    }
}

//--------------------AppUserDetail Interface

class CustomUserDetails(
    val id : Long,
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //val createdDate: Date,
    private val username: String,
    private val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    private val password: String?,
    val tokenExpired: Boolean,
    private val authorities: Collection<GrantedAuthority>,

) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String? = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}


@Service
@Transactional
class AppUserDetailsService(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository,
) : UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the `UserDetails`
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never `null`)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val normalizedUsername = username.trim().replace("\\s+".toRegex(), "").lowercase()

        val user: User = userRepository.findByEmail(normalizedUsername).orElse(null)
            ?: return CustomUserDetails(
                id = 0,
               // createdDate = Date(),
                username = "",
                enabled = true,
                firstName = "", // Valores predeterminados
                lastName = "",
                password = "",
                tokenExpired = false,
                authorities = getAuthorities(
                    listOf(
                        roleRepository.findByName("ROLE_USER").get()
                    )
                ),

            )

        return CustomUserDetails(
            id = user.id!!,
           // createdDate = user.createdDate,
            username = user.email,
            enabled = user.enabled,
            firstName = user.firstName,
            lastName = user.lastName,
            password = user.password,
            tokenExpired = user.tokenExpired,
            authorities = getAuthorities(user.roles!!.toMutableList()),
        )
    }

    private fun getAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        return roles.flatMap { role ->
            sequenceOf(SimpleGrantedAuthority(role.name)) +
                    role.privileges.map { privilege -> SimpleGrantedAuthority(privilege.name) }
        }.toList()
    }

}

/*Interface de EventProduct*/

interface EventProductService {
    fun findAllEventProducts(): List<EventProductDTO>
    fun findProductsByEventId(eventId: Long): List<ProductDTO>
}
@Service
class AbstractEventProductService(
    @Autowired
    val eventProductRepository: EventProductRepository,

    @Autowired
    val eventProductMapper: AllMapper,

    @Autowired
    val productMapper: AllMapper,
) : EventProductService {

    override fun findAllEventProducts(): List<EventProductDTO> {
        return eventProductMapper.eventProductListToEventProductDTOList(
            eventProductRepository.findAll()
        )
    }

    override fun findProductsByEventId(eventId: Long): List<ProductDTO> {
        val eventProducts = eventProductRepository.findByEvent_Id(eventId)
        return eventProducts.map { productMapper.productToProductDTO(it.product) }
    }
}

//--------------------Discount Interface
interface DiscountService {
    fun findDiscountById(id: Long): DiscountDTO?
    fun findDiscountByCode(code: String): DiscountDTO?
}

@Service
class AbstractDiscountService(
    @Autowired
    val discountRepository: DiscountRepository,

    @Autowired
    val discountMapper: AllMapper,
) : DiscountService {

    override fun findDiscountById(id: Long): DiscountDTO? {
        val discount: Optional<Discount> = discountRepository.findById(id)
        return discountMapper.discountToDiscountDTO(discount.get())
    }

    override fun findDiscountByCode(code: String): DiscountDTO? {
        val discount: Optional<Discount> = discountRepository.findByCode(code)
        return discountMapper.discountToDiscountDTO(discount.get())
    }
}

//--------------------Event Interface
interface EventService {
    fun findEventById(id: Long): EventDTO?
    fun findAllEvents(): List<EventDTO>?
}

@Service
class AbstractEventService(
    @Autowired
    val eventRepository: EventRepository,

    @Autowired
    val eventMapper: AllMapper,
) : EventService {

    override fun findEventById(id: Long): EventDTO? {
        val event: Optional<Event> = eventRepository.findById(id)
        return event.map { eventMapper.eventToEventDTO(it) }.orElse(null)
    }

    override fun findAllEvents(): List<EventDTO>? {
        val events: List<Event> = eventRepository.findAll()
        return events.map { eventMapper.eventToEventDTO(it) }
    }
}

//--------------------Order Interface

interface OrderService {
    fun findOrderById(id: Long): OrderDTO?
    fun findAllOrders(): List<OrderDTO>?
    fun findOrderDetailsByOrderId(orderId: Long): List<OrderDetailsDTO>?
    fun updateOrder(orderId: Long, statusId: Long): Boolean
    fun createOrderWithDetails(orderDTO: OrderDTO, orderDetailsList: List<OrderDetailsDTO>): OrderDTO
}

@Service
class AbstractOrderService(
    @Autowired
    private val orderRepository: OrderRepository,

    @Autowired
    private val userRepository: UserRepository,

    @Autowired
    private val orderMapper: AllMapper,

    @Autowired
    private val orderDetailRepository: OrderDetailRepository,

    @Autowired
    private val statusRepository: StatusRepository,
) : OrderService {

    // Find an order by ID
    override fun findOrderById(id: Long): OrderDTO? {
        val order: Optional<Order> = orderRepository.findById(id)
        return order.map { orderMapper.orderToOrderDTO(it) }.orElse(null)
    }

    // Find all orders
    override fun findAllOrders(): List<OrderDTO>? {
        return orderMapper.orderListToOrderDTOList(
            orderRepository.findAll()
        )
    }

    // Find order details by order ID
    override fun findOrderDetailsByOrderId(orderId: Long): List<OrderDetailsDTO>? {
        val order = orderRepository.findById(orderId).orElse(null)
        val orderDetails = orderDetailRepository.findByOrder(order)
        return orderDetails.map { orderMapper.orderDetailsToOrderDetailsResult(it) }
    }

    override fun updateOrder(orderId: Long, statusId: Long): Boolean {
        val orderOptional: Optional<Order> = orderRepository.findById(orderId)

        if (orderOptional.isEmpty) return false

        val order = orderOptional.get()

        val newStatusOptional: Optional<Status> = statusRepository.findById(statusId)
        if (newStatusOptional.isEmpty) return false

        val newStatus = newStatusOptional.get()

        order.status = newStatus

        return try {
            orderRepository.save(order)
            true // Operación exitosa
        } catch (e: Exception) {
            println("Error al actualizar la orden: ${e.message}")
            false // Operación fallida
        }
    }

    // Create a new order
    // Service implementation for creating an Order and OrderDetails
    override fun createOrderWithDetails(orderDTO: OrderDTO, orderDetailsList: List<OrderDetailsDTO>): OrderDTO {
        // Retrieve the User by id from the repository
        val user = orderDTO.user?.let {
            it.id?.let { it1 ->
                userRepository.findById(it1)
                    .orElseThrow { IllegalArgumentException("User not found") }
            }
        }

        // Map the OrderDTO to Order entity and set the persistent User instance
        val order = orderMapper.orderDTOToOrder(orderDTO)
        if (user != null) {
            order.user = user
        }

        // Save the order and retrieve the persisted instance with the generated id
        val savedOrder = orderRepository.save(order)

        // Map and assign the saved Order to each OrderDetail
        val orderDetailsEntities = orderDetailsList.map {
            val orderDetail = orderMapper.orderDetailDTOToOrderDetail(it)
            orderDetail.order = savedOrder // Set the persisted order with id
            orderDetail
        }

        // Save all OrderDetails
        orderDetailRepository.saveAll(orderDetailsEntities)

        // Return the saved order as DTO, including the assigned details
        return orderMapper.orderToOrderDTO(savedOrder)
    }

}


//--------------------User Interface


interface UserService {
    fun findUserByUsername(username: String): UserForOrderDTO?
    fun findAllUsers(): List<UserDTO>?
    fun createUser(user: UserDTO): UserDTOResult?
}

@Service
class AbstractUserService(
    @Autowired
    val userRepository: UserRepository,

    @Autowired
    val userMapper: AllMapper,

    private val passwordEncoder: BCryptPasswordEncoder,
) : UserService {

    override fun findUserByUsername(username: String): UserForOrderDTO? {
        val user : Optional<User> = userRepository.findByEmail(username)
        return user.map { userMapper.userForOrderToUserForOrderDTO(it) }.orElse(null)
    }

    override fun findAllUsers(): List<UserDTO>? {
        return userMapper.userListToUserDTOList(
            userRepository.findAll()
        )
    }

    override fun createUser(user: UserDTO): UserDTOResult? {
        val userWithDate = user.copy(createdDate = user.createdDate ?: Date())

        // Encrypt the password
        val userWithEncryptedPassword = userWithDate.copy(
            password = passwordEncoder.encode(user.password)
        )

        val userCreate = userMapper.userDTOToUser(userWithEncryptedPassword)
        return userRepository.save(userCreate).let { userMapper.userToUserResult(it) }
    }

}