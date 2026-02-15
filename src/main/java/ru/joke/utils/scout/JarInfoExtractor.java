package ru.joke.utils.scout;

import java.io.File;
import java.net.URL;
import java.util.function.Predicate;
import java.util.regex.Pattern;

final class JarInfoExtractor {

    private static final String JAR_SEPARATOR = "!";
    private static final Predicate<String> extensionPattern = Pattern.compile("\\.(jar|war|ear)$").asPredicate();

    String extractName(final URL url) {
        final var urlExt = url.toExternalForm();

        File jarFile = new File(urlExt);
        while (!jarFile.getPath().endsWith(JAR_SEPARATOR) && !extensionPattern.test(jarFile.getPath())) {
            jarFile = jarFile.getParentFile();
        }

        final var jarFileName = jarFile.getName();
        return jarFileName.endsWith(JAR_SEPARATOR)
                ? jarFileName.substring(0, jarFileName.length() - 1)
                : jarFileName;
    }
}
