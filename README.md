# README

## TL;DR

This project is a simplistic PoC to spin up a Spring Boot
REST application with a backing database, and a set of client
scripts to exercise it.
It is designed as a teaching tool for:

- Test Driven Design (TDD)
- Spring Boot concepts
- Deploying to a Cloud Native provider (PAS or Cloud Foundry)

For instant gratification, i.e. reviewing the solution code,
clone this repo, change to the project directory and reset to
the solution as follows:

```bash
git reset --hard solution
```

Review the code, then review the rest of this document at your
leisure for ideas how you might experiment with this code,
or perhaps use for your own learning or teaching concepts.

## Overview

This project is intended to be a short, 2-3 hours exercise to
demonstrate authoring a simple Spring Boot Web / CRUD application,
deploy,
monitor,
and operate it on Pivotal Cloud Foundry.

## Learning outcomes

-   Explain benefits of pairing and test-driven-design processes
-   Demonstrate how to bootstrap a brand new Spring Boot application
-   Demonstrate how to push an application to PCF
-   Demonstrate provisioning a database on PCF
-   Demonstrate an application consuming a database provided by PCF
-   Demonstrate operational features PCF provides to your Spring boot
    application running on it.

## Tagged checkpoints

The repository is setup up with revisionist history; i.e.
it has the full solution from start-to-finish,
yet the remote repository has HEAD pointed to the starting point,
or initial commit.
This may be confusing at first glance,
because the
[remote repo only shows one commit!](https://github.com/billkable/tracker/commits/master)

You can follow the history by executing the
[`git lola` aliased command](https://gist.github.com/BibMartin/3d8bc449ca2f93d4cf90),
and will see the following git log:

```nohighlight
* 803a0b2 (tag: timesheet-delete, tag: solution) timesheet-delete
* 604d6c4 (tag: timesheet-update-not-found) timesheet-update-not-found
* 6022323 (tag: timesheet-update) timesheet-update
* 378725a (tag: timesheet-find-not-found) timesheet-find-not-found
* 976a3f5 (tag: timesheet-find) timesheet-find
* 528be6b (tag: timesheet-create) timesheet-create
* 76f56be (HEAD -> master, tag: initial-commit, origin/master) initial-commit
```

The following describes the 8 checkpoints associated with the tags:

-   `initial-commit` -> includes this README, and a set of scripts
    and postman collection illustrating simple acceptance test for the
    project.
    There is a shell Spring Boot project that was generated through
    the Spring Initialzr at Spring Boot version 2.2.x.

-   `timesheet-create` -> includes the solution for the simple
    `tracker` Spring Boot application to save a timesheet
    entity to a MySql database.

-   `timesheet-find` -> includes the solution adding onto
    `timesheet-create`, for retrieving a timesheet by its id.

-   `timesheet-find-not-found` -> includes the final solution,
    adding onto `timesheet-find` with scenario when searching a
    timesheet that does not exist.

-   `timesheet-update` -> includes the solution adding onto
    `timesheet-find-not-found`, for updating and existing timesheet.

-   `timesheet-update-not-found` -> includes the solution adding onto
    `timesheet-update`, for attempting to update a timesheet that
    does not exist.

-   `timesheet-delete` or `solution` -> includes the solution adding onto
    `timesheet-update-not-found`, for deleting a timesheet.

If you are not working the exercises,
but only wanting to review each solution step,
merely reset HEAD to the appropriate tag or commit:

```bash
git reset --hard {tag}
```

## Tracker Project Overview

The application uses Spring Boot with the following starters:

- Spring Web MVC
- Spring Data JDBC for persistence
- Spring Boot Actuator
- Flyway for managing the intial migration (if necessary)
- MySQL database driver

A Postman collection is provided as acceptance test,
as well as means of interactively testing your application.

A Jmeter test plan is provided for a simple idempotent load injector
to demonstrate behavior under load,
and for auto-scaling behaviors.

Disclaimer:
the solution is naive,
intended for fast simple solution to demonstrate basic concepts,
do not use for production use.

## Prerequisites

Developer workstation requires:

-   Java 8 or 11
-   IDE of choice (IntelliJ preferred)
-   `git` client
-   `cf` client
-   Network proxy setup done before-hand:
    -   Access to Maven Central,
        or internal Maven Repo with access to Spring Boot 2.1.10
        curated dependencies
    -   Access to either PWS,
        or PCF with `p.mysql` service title installed
-   MySQL databases provisioned
    -   Local (docker setup supplied in `scripts/db-docker`)
    -   Cloud Foundry (initialization script in `scripts/db-cf`,
        or generated through the verification `scripts/verify.sh`)

### Verification

A prerequisite verification script is provided for you
at `scripts/verify.sh`.
It will verify your ability to build and push your tracker
application with a MySQL database:

1.  Login to Cloud Foundry cf-cli,
    and target your development workspace:

    ```bash
    cf login -a {apiurl} -o {organization} -s {space} -u {username}
    ```

    You must provide a password interactively

1.  From the `tracker` project root,
    execute the verification script:

    ```bash
    ./scripts/verify.sh {mysql db service} {mysql db service plan}
    ```

    For example,
    on PWS:

    ```bash
    ./scripts/verify.sh cleardb spark
    ```

    On PCF with `p.mysql` tile installed:

    ```bash
    ./scripts/verify.sh p.mysql db-small
    ```

## Suggested workshop flow

1.  Instructors deliver introduction narrative
    (15 min):

    - Modern practices
    - Modern platforms

1.  Instructors and Students bootstrap from the
    [Spring Initializr](https://start.spring.io)
    with the following starters together:

    - Spring Web MVC
    - Spring Data JDBC for persistence
    - Spring Boot Actuator
    - Flyway for managing the intial migration
    - MySQL database driver

    (5 min)

1.  If the student is relatively experienced,
    have they/she/he initalize a project in IntelliJ with the starters
    mentioned above,
    to the `~/workspace/tracker` directory.

1.  Have each student initialize a git repository in the
    `~/workspace/tracker` directory,
    point the remote to this repo,
    and pull.
    Student will get the shell and postman scripts
    accompanying the tracker application.

    (5 min)

1.  Instructors will demonstrate:
    -   Pairing and TDD to generate the create timesheet operation
        and database migration.
    -   A pregenerated PCF manifest.
    -   Push application to PCF.
    -   Run the migration as a one-off task.
    -   Demonstrate Create operation with the Postman script.
    -   Walk through with the student what is going on with Cloud
        Foundry PAS,
        with the DB service instance,
        service binding and route.

    (30 min)

1.  Students execute a `git reset --hard timesheet-create` to pull in
    the solution point to the tracker application with the create
    timesheet operation,
    and database migration runner already provided.

    (5 min)

1.  Each Student creates a database instance on their PCF space,
    if not already created.

1.  Each Student builds and pushes the application,
    and run the database migration as a 1-off admin task.

    (15 min)

1.  Students work through solution to implement the Read operation,
    using TDD against the `TimesheetsControllerTests` for unit testing,
    using the existing mockito and controller fixtures from the Create
    test.

    Implement `TimesheetsApplicationTests` for integration testing.

    The solution is the provided to students for future study
    as the `timesheet-find` tag.

    (40 min)

1.  Instructors demo:

    -   Demonstrate correlated Create and Read with Postman script.
    -   Running load injector on solution target:
        -   Load injector plan supplied for simple get-by-id operation.
        -   App Manager / Spring Boot actuator integration.
        -   Logs
        -   Scaling
        -   Availability
            (demonstrate termination and recovery of tracker app)
        -   Autoscaling (if enabled on platform)

    (20 min)

1.  Instructors discuss some elements of 12-factor in context of
    the work session:

    - Configuration
    - Disposability
    - Concurrency
    - Backing Services
    - Build, release, run
    - Logs
    - Admin processes

    (10 min)

1.  Instructors point out naive design of read operation,
    use TDD to implement when a timesheet entity does not exists.

    The solution is the `timesheet-find-not-found` tag.

    (5 min)

1.  If time permits,
    have students:
    - Implement find-not-found
    - Implement update
    - Implement delete
    - Experiment with PCF scaling and availability
    - Experiment with App Manager monitoring and logs
    - Q&A with instructors
