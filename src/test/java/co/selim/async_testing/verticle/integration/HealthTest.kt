package co.selim.async_testing.verticle.integration

import co.selim.async_testing.MainVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.coroutines.await
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class HealthTest(vertx: Vertx) : IntegrationTest(vertx, MainVerticle::class) {
  @Test
  fun dbIsUp() = runTest {
    val request = httpClient.request(HttpMethod.GET, 8080, "localhost", "/db-health").await()
    val response = request.send().await()
    val responseJson = response.body().await().toJsonObject()

    Assertions.assertEquals("up", responseJson.getString("postgres"))
  }
}
