An in memory database is a powerful tool to decouple a system from a database dependency. Sometimes we need to have access to the database from another process. For example this is the case if we execute funcional tests against the html interface of a web-application. We need to re-initialize the database before each test. 

[H2 database](http://www.h2database.com) offers a tcpServer for this purpose. It enables us to make a jdbc connection to the memory-db using a url like `jdbc:h2:tcp://localhost:9092/mem:testdb`. The `h2-web-jpa-datasource-demo` project shows one way to start the server on the application server level in the datasource.

h2-demo-java
: Shows how to start an h2 in-mem db and to connect from outside. See instrucions in the `H2Demo.java`.

h2-web-hibernate-demo
: How to connect to h2 in-mem db inside web application managing its own connections. Execute `mvn jetty:run` and go <http://localhost:8080/>. Using for example [Squirrel](http://squirrel-sql.sourceforge.net/) connect to `jdbc:h2:tcp://localhost:9092/mem:demodb;IFEXISTS=TRUE`

h2-tcp-datasource
: H2 specific datasource to use in h2-web-jpa-datasource-demo

h2-web-jpa-datasource-demo
: Shows how to use a customized datasource to connect to h2 in-mem db in case of application server managed datasource. Execute `mvn jetty:run` and go <http://localhost:8080/>. Using for example [Squirrel](http://squirrel-sql.sourceforge.net/) connect to `jdbc:h2:tcp://localhost:9092/mem:demodb;IFEXISTS=TRUE`
