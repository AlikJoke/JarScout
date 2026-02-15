package ru.joke.utils.scout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

final class JarCollector {

    private static final Set<String> ARCHIVE_EXTENSIONS = Set.of(".jar", ".war", ".ear");

    private final JarInfoExtractor jarInfoExtractor;

    JarCollector(final JarInfoExtractor jarInfoExtractor) {
        this.jarInfoExtractor = jarInfoExtractor;
    }

    Set<String> collect(final String path) {
        try {
            final var jarUrl = new File(path).toURI().toURL();
            return collect(jarUrl);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Set<String> collect(final URL jarUrl) throws IOException {
        final Set<String> foundJars = new HashSet<>();

        foundJars.add(this.jarInfoExtractor.extractName(jarUrl));
        processJar(jarUrl, foundJars);

        return foundJars;
    }

    private void processJar(final URL jarUrl, final Set<String> foundJars) throws IOException {
        try (final var is = jarUrl.openStream();
             final var zis = new ZipInputStream(new BufferedInputStream(is))) {
            processEntries(zis, foundJars);
        }
    }

    private void processEntries(
            final ZipInputStream zis,
            final Set<String> foundJars
    ) throws IOException {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            final var name = entry.getName();

            final var extensionPos = name.lastIndexOf(".");
            final var extension = extensionPos == -1 ? null : name.substring(extensionPos);

            if (extension != null && ARCHIVE_EXTENSIONS.contains(extension)) {
                foundJars.add(extractFileName(name));
                collectNested(zis, foundJars);
            }

            zis.closeEntry();
        }
    }

    private void collectNested(
            final ZipInputStream parentStream,
            final Set<String> foundJars
    ) throws IOException {

        final ZipInputStream nestedZis = new ZipInputStream(parentStream) {
            @Override
            public void close() {
            }
        };

        processEntries(nestedZis, foundJars);
    }

    private String extractFileName(String fullPath) {
        return fullPath.substring(fullPath.lastIndexOf('/') + 1);
    }
}
