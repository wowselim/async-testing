package co.selim.async_testing.comparison

import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class KotlinHttpServerTest {
  @Test
  @Timeout(5, unit = TimeUnit.SECONDS)
  fun `start http server`(vertx: Vertx): Unit = runBlocking(vertx.dispatcher()) {
    vertx.createHttpServer()
      .requestHandler { req -> req.response().end() }
      .listen(16969)
      .await()
  }
}
