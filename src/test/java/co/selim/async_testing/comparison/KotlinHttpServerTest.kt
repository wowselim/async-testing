package co.selim.async_testing.comparison

import co.selim.async_testing.MainVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class KotlinHttpServerTest {
  private lateinit var deploymentId: String

  @BeforeEach
  fun deployVerticle(vertx: Vertx): Unit = runBlocking(vertx.dispatcher()) {
    deploymentId = vertx.deployVerticle(MainVerticle()).await()
  }

  @AfterEach
  fun undeployVerticle(vertx: Vertx): Unit = runBlocking(vertx.dispatcher()) {
    vertx.undeploy(deploymentId)
  }

  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  fun `service is healthy`(vertx: Vertx): Unit = runBlocking(vertx.dispatcher()) {
    vertx.deployVerticle(MainVerticle())
    val httpClient = vertx.createHttpClient()

    val request = httpClient.request(HttpMethod.GET, 8080, "localhost", "/health").await()
    val response = request.send().await()
    val responseJson = response.body().await().toJsonObject()

    Assertions.assertEquals("up", responseJson.getString("status"))
  }
}
