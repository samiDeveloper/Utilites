Aggregates multiple functional modules in one Java web application

A module is the implementation of a bounded context.
A module is independent and functionally cohesive, isolated as much as possible from other modules and its environment.
This small demo system has two modules, customer and order, both defining their own JPA persistence unit.

build: `multi-context-demo> mvn install`

run: `multi-context-demo/multi-ctx-web> mvn jetty:run`

test: <http://localhost:8080/>

To find proof that this demo application accually persists: connect to the in memory database using a sql client, for example [SQuirrel](http://squirrel-sql.sourceforge.net/), and connect to `jdbc:h2:tcp://localhost:9092/mem:multi-ctx-db;IFEXISTS=TRUE` using empty username and password

Technologies used:

- java 8
- servlet 3 using ServletContainerInitializer instead of web.xml
- spring 4.1.6 using annotation only
- jpa / hibernate / h2 db
- jetty 9
- lombok

