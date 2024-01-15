package org.acme.service

import io.quarkus.narayana.jta.QuarkusTransaction
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.persistence.BookEntity
import org.acme.persistence.BookRepository

@ApplicationScoped
class UpdateBookService {
    @Inject
    private lateinit var bookRepository: BookRepository

    fun updateBookWithTransaction(bookId : Long, newTitle: String) {
        QuarkusTransaction.begin()
        val bookEntity = bookRepository.findByBookId(bookId = bookId)
        bookEntity?.title = newTitle
        QuarkusTransaction.commit()
    }


}

@ApplicationScoped
class GetBookWithoutTransactionService {
    @Inject
    private lateinit var bookRepository: BookRepository

    fun getBookById(id: Long) : BookEntity? = bookRepository.findByBookId(id)

}

@ApplicationScoped
class GetBookWithTransactionService {
    @Inject
    private lateinit var bookRepository: BookRepository

    @Transactional
    fun getBookById(id: Long) : BookEntity? = bookRepository.findByBookId(id)
}