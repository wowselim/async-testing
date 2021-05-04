# Testing Vert.x Applications in Kotlin

[![](https://img.shields.io/badge/vert.x-4.0.3-purple.svg)](https://vertx.io)
![Build](https://github.com/wowselim/async-testing/actions/workflows/build.yml/badge.svg)

## Structure

This project consists of a simple verticle that provides two api endpoints:

* `/health` replies with `{"status": "up"}` to indicate that the service is up and running.
* `/db-health` replies with either `{"status": "up"}`, or `{"status": "down"}`, depending on database connectivity.

It also includes the following example tests as a starting point for testing Vert.x applications in Kotlin.

1. [Comparison between Java and Kotlin test code](src/test/java/co/selim/async_testing/comparison)
2. [Testing with full Verticle deployment](src/test/java/co/selim/async_testing/verticle)
3. [Integration testing with a containerized database](src/test/java/co/selim/async_testing/verticle/integration)
