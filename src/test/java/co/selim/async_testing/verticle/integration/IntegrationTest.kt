package co.selim.async_testing.verticle.integration

import co.selim.async_testing.verticle.AsyncTest
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.reflect.KClass

private const val DB_NAME = "integration"
private const val DB_USERNAME = "integration"
private const val DB_PASSWORD = "integration"

@Testcontainers
@ExtendWith(VertxExtension::class)
abstract class IntegrationTest(
  private val vertx: Vertx,
  private val verticleClass: KClass<out Verticle>,
  private val deploymentOptions: DeploymentOptions = DeploymentOptions()
) : AsyncTest(vertx, verticleClass, deploymentOptions) {
  companion object {
    @Container
    private var postgresqlContainer: PostgreSQLContainer<Nothing> = PostgreSQLContainer<Nothing>("postgres:12")
      .apply {
        withDatabaseName(DB_NAME)
        withUsername(DB_USERNAME)
        withPassword(DB_PASSWORD)
      }
  }

  override fun deployVerticle(): String = runBlocking(vertx.dispatcher()) {
    val dbUri = postgresqlContainer.jdbcUrl.substringAfter("jdbc:")
    deploymentOptions.config = jsonObjectOf(
      "db.uri" to dbUri,
      "db.username" to DB_USERNAME,
      "db.password" to DB_PASSWORD,
    )
    vertx.deployVerticle(verticleClass.java, deploymentOptions).await()
  }
}
