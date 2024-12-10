package com.project.dessertapp.model.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.project.dessertapp.model.entities.Category
import com.project.dessertapp.model.entities.Discount
import com.project.dessertapp.model.entities.Event
import com.project.dessertapp.model.entities.Order
import com.project.dessertapp.model.entities.OrderDetails
import com.project.dessertapp.model.entities.Product
import com.project.dessertapp.model.entities.Status
import com.project.dessertapp.model.entities.User
import com.project.dessertapp.model.network.category.CategoryDeserializer
import com.project.dessertapp.model.network.event.EventDeserializer
import com.project.dessertapp.model.network.event.EventService
import com.project.dessertapp.model.network.category.CategoryService
import com.project.dessertapp.model.network.order.DiscountDeserializer
import com.project.dessertapp.model.network.order.OrderDeserializer
import com.project.dessertapp.model.network.order.OrderDetailDeserializer
import com.project.dessertapp.model.network.order.OrderService
import com.project.dessertapp.model.network.order.StatusDeserializer
import com.project.dessertapp.model.network.product.ProductDeserializer
import com.project.dessertapp.model.network.product.ProductService
import com.project.dessertapp.model.network.user.UserDeserializer
import com.project.dessertapp.model.network.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module to provide network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of Gson configured with a custom deserializer for Task.
     *
     * @return A configured Gson instance.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Product::class.java, ProductDeserializer()) // Deserializer for Product
            .registerTypeAdapter(Category::class.java, CategoryDeserializer()) // Deserializer for Category
            .registerTypeAdapter(Event::class.java, EventDeserializer()) // Deserializer for Event
            .registerTypeAdapter(User::class.java, UserDeserializer())  // Deserializer for User
            .registerTypeAdapter(Order::class.java, OrderDeserializer())  // Deserializer for Order
            .registerTypeAdapter(OrderDetails::class.java, OrderDetailDeserializer())
            .registerTypeAdapter(Discount::class.java, DiscountDeserializer())
            .registerTypeAdapter(Status::class.java, StatusDeserializer())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .create()
    }

    /**
     * Provides a singleton instance of HttpLoggingInterceptor for logging HTTP requests and responses.
     *
     * @return A configured HttpLoggingInterceptor instance.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides a singleton instance of OkHttpClient configured with the logging interceptor.
     *
     * @param loggingInterceptor The HttpLoggingInterceptor instance.
     * @return A configured OkHttpClient instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    /**
     * Provides a singleton instance of Retrofit configured with the base URL, Gson converter, and OkHttpClient.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param gson The Gson instance.
     * @return A configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .create()

        return Retrofit.Builder()
            // For dev
            //.baseUrl("http://10.0.2.2:8080/v1/")

            // For production
            .baseUrl("https://dessert-app-8a5756c10fa8.herokuapp.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides a singleton instance of ProductService created by Retrofit.
     *
     * @param retrofit The Retrofit instance.
     * @return A ProductService instance.
     */
    @Provides
    @Singleton
    fun provideProductService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    /**
     * Provides a singleton instance of CategoryService created by Retrofit (if you need it).
     *
     * @param retrofit The Retrofit instance.
     * @return A CategoryService instance.
     */
    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    /**
     * Provides a singleton instance of EventService created by Retrofit (if you need it).
     *
     * @param retrofit The Retrofit instance.
     * @return A EventService instance.
     */
    @Provides
    @Singleton
    fun provideEventService(retrofit: Retrofit): EventService {
        return retrofit.create(EventService::class.java)
    }

    /**
     * Provides a singleton instance of UserService created by Retrofit.
     *
     * @param retrofit The Retrofit instance.
     * @return A UserService instance.
     */
    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    /**
     * Provides a singleton instance of OrderService created by Retrofit.
     *
     * @param retrofit The Retrofit instance.
     * @return A OrderService instance.
     */
    @Provides
    @Singleton
    fun provideOrderService(retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }
}