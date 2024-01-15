package org.acme.persistence

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookRepository : NoCoveragePanacheRepository<BookEntity>() {
    fun findByBookId(bookId: Long) : BookEntity? = list("id = ?1", bookId).firstOrNull()
}

/**
 * Represents a Repository for a specific type of entity [T].
 * Extending this abstract class will offer same useful functions that are on [PanacheRepository] but
 * without code coverage introspection of these provided functions.
 *
 * @param T The type of entity to operate on
 */
abstract class NoCoveragePanacheRepository<T : Any> : PanacheRepository<T>