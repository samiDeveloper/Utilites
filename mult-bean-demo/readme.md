This project shows a scenario where spring Java config instantiates multiple singleton beans

Uses Java 8 and maven

[This SO question](http://stackoverflow.com/questions/26641349/why-does-this-java-based-spring-configuration-create-two-instances-of-a-singleto) has the answer that explains the Spring behaviour. It is the 'lite mode'

usage:

    mvn compile
    mvn exec:java