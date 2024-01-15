package org.acme

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

class DatabaseResource : QuarkusTestResourceLifecycleManager {

    private val postgreSQLContainer = PostgreSQLContainer("postgres:14.2")
        .withDatabaseName("book_db")
        .withUsername("username")
        .withPassword("password")
        .withExposedPorts(5432)

    override fun start(): Map<String?, String?>? {
        postgreSQLContainer.start()
        return Collections.singletonMap("quarkus.datasource.jdbc.url", postgreSQLContainer.jdbcUrl)
    }

    override fun stop() {
        postgreSQLContainer.stop()
    }
}