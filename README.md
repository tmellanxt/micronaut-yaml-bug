# 🧪 Micronaut Configuration Override Issue — Demonstration Project

This repository demonstrates a subtle but critical issue in Micronaut's configuration loading mechanism when using multi-module Gradle projects. Specifically, it shows how an `application.yml` file in a library module can **silently override** the main application's `application.yaml`, depending on classpath order and file extension.

---

## ❗ Problem Overview

Micronaut advertises support for configuration **merging** from multiple sources (e.g., system properties, environment-specific files, and application configs). However, it does **not merge multiple `application.yml` or `application.yaml` files across modules** if they share the same base name.

Instead, it uses the first file found on the classpath based on file extension and stops, discarding others — often **without warning**.

This becomes a serious issue when:

- A library module (e.g., a shared internal dependency) includes its own `application.yml`
- The main application defines `application.yaml`
- Micronaut only loads the library's config, ignoring the app's configuration

---

## 🎯 Goal of This Project

This project is designed to:

- Reproduce the exact conditions where the override occurs
- Show how Micronaut loads only one configuration file from the classpath for `application.yml` or `application.yaml`
- Demonstrate how the main application config can be ignored due to a library's internal config
- Raise awareness of the lack of warnings or visibility into this behavior

---

## 🚀 How to Reproduce

1. Clone this repository.
2. Run the application using `./gradlew :list:run`.
3. Observe that configuration values defined in the application’s `application.yaml` are **missing or overridden**. This will be printed out on application start up. 
4. Comment out the dependency on the `utilities` module and rerun the application — your config now takes effect as expected.

---

## 🧠 Technical Notes

- Micronaut uses `PropertySourceLoader` implementations (like `YamlPropertySourceLoader`) that extend `AbstractPropertySourceLoader`.
- These loaders call `getResourceAsStream()` and stop after finding the **first matching** `application.yml` or `application.yaml` file on the classpath.
- No merging is done between identically named files across modules or JARs.
- The order in which Micronaut finds files depends on the classpath — which can vary subtly depending on how dependencies are declared and assembled.

---

## 📣 Why This Matters

This behavior is unintuitive and easy to miss. It can:

- Cause application configs to be **silently ignored**
- Lead to long debugging sessions and misconfiguration in production
- Make modular design with reusable config-bearing libraries risky

---

## Project Structure

- `list`: The main application module.
- `utilities`: A simple library module.

## Configuration

Both modules have their own `application.yml` files located in their respective `src/main/resources` directories.

### `list` Module

The `list` module contains the main application. It includes a class `OnStartUp` that reads a configuration value from the `application.yml` file and prints it to the console when the application starts.

### `utilities` Module

The `utilities` module is a simple library that can be used by the main application.

## Running the Application

To run the application, use the following command:

```sh
./gradlew :list:run
