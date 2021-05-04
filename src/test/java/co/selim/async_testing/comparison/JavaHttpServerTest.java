package co.selim.async_testing.comparison;

import co.selim.async_testing.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;


@ExtendWith(VertxExtension.class)
public class JavaHttpServerTest {
  @Test
  @Timeout(value = 5, timeUnit = TimeUnit.SECONDS)
  void service_is_healthy(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle());
    HttpClient httpClient = vertx.createHttpClient();

    httpClient.request(HttpMethod.GET, 8080, "localhost", "/health")
      .compose(HttpClientRequest::send)
      .compose(HttpClientResponse::body)
      .onComplete(testContext.succeeding(buffer -> {
        JsonObject responseJson = buffer.toJsonObject();

        Assertions.assertEquals("up", responseJson.getString("status"));
        testContext.completeNow();
      }));
  }
}
