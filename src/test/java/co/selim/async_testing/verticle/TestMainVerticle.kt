package co.selim.async_testing.verticle

import co.selim.async_testing.MainVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.coroutines.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class TestMainVerticle(vertx: Vertx) : AsyncTest(vertx, MainVerticle::class) {

  @Test
  fun `service is healthy when deployed`() = runTest {
    val request = httpClient.request(HttpMethod.GET, 8080, "localhost", "/health").await()
    val response = request.send().await()
    val responseJson = response.body().await().toJsonObject()

    assertEquals("up", responseJson.getString("status"))
  }
}
