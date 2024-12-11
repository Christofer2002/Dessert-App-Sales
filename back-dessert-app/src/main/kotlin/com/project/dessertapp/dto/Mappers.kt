package com.project.dessertapp.dto

import com.project.dessertapp.entities.core.Privilege
import com.project.dessertapp.entities.core.Role
import com.project.dessertapp.entities.core.User
import com.project.dessertapp.entities.event.Event
import com.project.dessertapp.entities.order.Order
import com.project.dessertapp.entities.order.OrderDetail
import com.project.dessertapp.entities.product.Category
import com.project.dessertapp.entities.product.Discount
import com.project.dessertapp.entities.product.Product
import com.project.dessertapp.entities.associations.EventProduct
import com.project.dessertapp.entities.product.Taste
import org.mapstruct.*
import java.util.*


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AllMapper {
    // Convert OrderDTO to Order entity
    fun orderDTOToOrder(orderDTO: OrderDTO): Order

    // Convert Order entity to OrderDTO for the returned result
    fun orderToOrderDTO(order: Order): OrderDTO

    // Convert a list of Order entities to a list of OrderDTOs
    fun orderListToOrderDTOList(orders: List<Order>): List<OrderDTO>
  
     // Mapping for OrderDetails entities
    fun orderDetailDTOToOrderDetail(orderDetailDTO: OrderDetailsDTO): OrderDetail

    // Mapping for saved OrderDetails to result DTOs
    fun orderDetailsToOrderDetailsResult(orderDetail: OrderDetail): OrderDetailsDTO

    fun userToUserResult(
        user: User
    ): UserDTOResult

    fun userToUserDTO(
        user: User
    ): UserDTO

    fun userForOrderToUserForOrderDTO(
        user: User
    ): UserForOrderDTO

    fun userListToUserDTOList(
        user: List<User>
    ) : List<UserDTO>

    fun orderDetailToOrderDetailDTO(
        orderDetail: OrderDetail
    ) : OrderDetailsDTO

    fun categoryToCategoryDTO(
        category: Category
    ) : CategoryDTO

    fun categoryToCategoryResult(
        category: Category
    ) : CategoryDTOResult

    fun productToProductDTO(
        product: Product
    ) : ProductDTO

    fun productToProductResult(
        product: Product
    ) : ProductDTOResult

    fun productDTOToProduct(
        product: ProductDTO
    ) : Product

    fun categoryDTOToCategory(
        category: CategoryDTO
    ) : Category

    fun productListToProductDTOList(
        product: List<Product>
    ) : List<ProductDTO>

    fun eventProductListToEventProductDTOList(
        eventProduct: List<EventProduct>
    ) : List<EventProductDTO>

    fun categoryListToCategoryDTOList(
        category: List<Category>
    ) : List<CategoryDTO>

    fun tasteListToTasteDTOList(
        taste: List<Taste>
    ) : List<TasteDTO>

    fun tasteToTasteDTO(
        taste: Taste
    ) : TasteDTO

    fun tasteDTOToTaste(
        taste: TasteDTO
    ) : Taste

    fun userDTOToUser(
        user: UserDTO
    ) : User

    fun tasteToTasteResult(
        taste: Taste
    ) : TasteDTOResult

    fun discountToDiscountDTO(
        discount: Discount
    ) : DiscountDTO

    fun eventToEventDTO(
        event: Event
    ) : EventDTO

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun productToProduct(dto: ProductDTO, @MappingTarget task: Product)

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun categoryToCategory(dto: CategoryDTO, @MappingTarget category: Category)

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun tasteToTaste(dto: TasteDTO, @MappingTarget taste: Taste)

}
