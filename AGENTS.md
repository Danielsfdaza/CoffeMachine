# Repository Guidelines

## Project Structure & Module Organization

This repository contains a Java/Gradle coffee machine system. The active Gradle root is `coffeemach/`, with `settings.gradle` currently including `ServidorCentral` and `coffeeMach`. Additional modules, `cmLogistics` and `bodegaCentral`, exist but are commented out in `settings.gradle`; enable them there before expecting Gradle to build them.

Source code lives under each module's `src/main/java`, and runtime configuration lives under `src/main/resources` (`server.cfg`, `coffeMach.cfg`, `CmLogistic.cfg`). Shared ZeroC Ice Slice definitions are in `coffeemach/CoffeMach.ice`; generated Java files appear under module `build/generated-src` and should not be edited directly. Database setup scripts are in `scripts/postgres`.

## Build, Test, and Development Commands

Run commands from `coffeemach/`:

```powershell
.\gradlew.bat build
```

Builds all included modules, generates Ice bindings, compiles Java, and creates JARs under each module's `build/libs`.

```powershell
.\gradlew.bat test
```

Runs Gradle's Java test task. No test sources are currently present, so add tests under `src/test/java` before relying on this as meaningful validation.

```powershell
java -jar ServidorCentral\build\libs\ServidorCentral.jar
java -jar coffeeMach\build\libs\coffeeMach.jar
```

Runs the built server and machine clients after a successful build. Ensure PostgreSQL is prepared first using `scripts/postgres/coffeemach-user.sql`, `coffeemach-ddl.sql`, then `coffeemach-inserts.sql`.

## Coding Style & Naming Conventions

Use standard Java conventions: 4-space indentation, `PascalCase` classes, `camelCase` methods and fields, and package names in lowercase. Keep module-specific code inside its module package tree. Do not modify compiled output in `bin/`, `build/`, or generated Ice sources; update `CoffeMach.ice` and regenerate through Gradle instead.

## Testing Guidelines

Use JUnit-compatible Gradle tests under `src/test/java` when adding coverage. Name test classes after the unit under test, for example `VentasManagerTest` or `DepositoMonedasTest`. Prefer focused unit tests for repositories, models, and service logic; integration tests that require PostgreSQL should document required seed scripts.

## Commit & Pull Request Guidelines

No Git history is available in this checkout, so no existing commit convention can be inferred. Use concise imperative commits such as `Add inventory validation` or `Fix coffee machine sale flow`. Pull requests should describe the behavior changed, list build/test commands run, mention database or configuration changes, and include screenshots for GUI changes.

## Agent-Specific Instructions

Keep changes scoped to source, config, Gradle, or SQL files. Treat generated and compiled artifacts as disposable build output unless the user explicitly asks otherwise.
