package co.selim.async_testing.verticle

import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClient
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.reflect.KClass

abstract class AsyncTest(
  private val vertx: Vertx,
  private val verticleClass: KClass<out Verticle>,
  private val deploymentOptions: DeploymentOptions = DeploymentOptions()
) {
  private lateinit var deploymentId: String
  protected val httpClient: HttpClient by lazy { vertx.createHttpClient() }

  open fun deployVerticle(): String = runBlocking(vertx.dispatcher()) {
    vertx.deployVerticle(verticleClass.java, deploymentOptions).await()
  }

  @BeforeEach
  fun assignDeploymentId() {
    deploymentId = deployVerticle()
  }

  @AfterEach
  fun undeployVerticle(): Unit = runBlocking(vertx.dispatcher()) {
    vertx.undeploy(deploymentId).await()
  }

  protected fun runTest(block: suspend () -> Unit): Unit = runBlocking(vertx.dispatcher()) {
    block()
  }
}
