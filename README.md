# JarScout
<b>JarScout</b> is a library designed to identify unused dependencies within Java distributions. 
It operates as a Java agent and generates a report on used and unused JAR files upon JVM shutdown.

The analysis is performed by intercepting the JVM class-loading process and identifying the source JAR for each loaded class. 
If at least one class from a specific JAR is loaded, that JAR is marked as "used" and removed from 
the "unused" list. The initial list of JAR files is extracted from the distribution archive, 
the path to which must be specified in the Java agent arguments.

The Java agent supports the following parameters:
1. ```output.file.path```: Specifies the path to the file where the final report on used and unused JARs will be saved.
2. ```artifact.path```: Specifies the path to the distribution archive (in .jar, .war, or .ear format) to be analyzed.

Example of Java agent launch parameters:
```java -javaagent:/path/to/jar/jar-scout.jar=artifact.path=/path/to/jar/test.jar,output.file.path=/path/to/output/out.txt -jar test.jar```