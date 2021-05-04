package co.selim.async_testing

import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.get
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions

class MainVerticle : CoroutineVerticle() {

  private val pool: Pool by lazy {
    val connectOptions = PgConnectOptions.fromUri(config["db.uri"])
      .setUser(config["db.username"])
      .setPassword(config["db.password"])
    PgPool.pool(vertx, connectOptions, PoolOptions())
  }

  override suspend fun start() {
    val router = Router.router(vertx)

    router.get("/health")
      .handler { ctx ->
        val response = jsonObjectOf("status" to "up")

        ctx.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(response.encode())
      }

    router.get("/db-health")
      .handler { ctx ->
        pool.query("SELECT 1")
          .execute()
          .onComplete { result ->
            val status = if (result.succeeded()) {
              "up"
            } else {
              "down"
            }

            val response = jsonObjectOf("postgres" to status)
            ctx.response()
              .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
              .end(response.encode())
          }
      }

    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080)
      .await()
  }
}
