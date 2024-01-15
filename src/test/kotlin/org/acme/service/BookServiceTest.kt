package org.acme.service

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.DatabaseResource
import org.acme.persistence.BookEntity
import org.acme.persistence.BookRepository
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.`is` as Is

@QuarkusTest
@QuarkusTestResource(DatabaseResource::class)
class BookServiceTest {

    @Inject
    lateinit var updateBookService: UpdateBookService

    @Inject
    lateinit var getBookWithoutTransactionService: GetBookWithoutTransactionService

    @Inject
    lateinit var getBookWithTransactionService: GetBookWithTransactionService

    @Inject
    lateinit var bookRepository: BookRepository

    lateinit var bookEntity: BookEntity

    @AfterEach
    @Transactional
    fun cleanDatabase() {
        bookRepository.deleteAll()
    }

    @BeforeEach
    @Transactional
    fun initializeDatabase() {
        bookEntity = BookEntity(title = "Title 1")
        bookRepository.persist(bookEntity)
    }

    @Test
    fun `Get updated entity with transaction`() {
        // Get book from repository once
        val bookEntityFromRepository = getBookWithTransactionService.getBookById(bookEntity.id!!)
        assertThat(bookEntityFromRepository, Is(bookEntity))

        // Update book title
        updateBookService.updateBookWithTransaction(bookId = bookEntity.id!!, newTitle = "New Title 2")

        // Get updated book from repository
        val bookEntityWithTransaction = getBookWithTransactionService.getBookById(bookEntity.id!!)
        val expectedUpdatedBookEntity = BookEntity(id= bookEntity.id, title = "New Title 2")
        assertThat(bookEntityWithTransaction, Is(expectedUpdatedBookEntity))
    }

    @Test
    fun `Get updated entity without transaction`() {
        // Get book from repository once
        val bookEntityFromRepository = getBookWithoutTransactionService.getBookById(bookEntity.id!!)
        assertThat(bookEntityFromRepository, Is(bookEntity))

        // Update book title
        updateBookService.updateBookWithTransaction(bookId = bookEntity.id!!, newTitle = "New Title 2")

        // Get updated book from repository
        val bookEntityWithoutTransaction = getBookWithoutTransactionService.getBookById(bookEntity.id!!)
        val expectedUpdatedBookEntity = BookEntity(id= bookEntity.id, title = "New Title 2")
        assertThat(bookEntityWithoutTransaction, Is(expectedUpdatedBookEntity))
    }

    @Test
    fun `Get updated entity without transaction but with cleared cache`() {
        // Get book from repository once
        val bookEntityFromRepository = getBookWithoutTransactionService.getBookById(bookEntity.id!!)
        assertThat(bookEntityFromRepository, Is(bookEntity))

        // Update book title
        updateBookService.updateBookWithTransaction(bookId = bookEntity.id!!, newTitle = "New Title 2")

        // Clear repository cache
        bookRepository.getEntityManager().clear()

        // Get updated book from repository
        val bookEntityWithoutTransaction = getBookWithoutTransactionService.getBookById(bookEntity.id!!)
        val expectedUpdatedBookEntity = BookEntity(id= bookEntity.id, title = "New Title 2")
        assertThat(bookEntityWithoutTransaction, Is(expectedUpdatedBookEntity))
    }
}