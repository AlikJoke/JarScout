package ru.joke.utils.scout;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Optional;

final class JarScoutTransformer implements ClassFileTransformer {

    private final ScoutData data;
    private final JarInfoExtractor jarInfoExtractor;

    JarScoutTransformer(
            final ScoutData data,
            final JarInfoExtractor jarInfoExtractor
    ) {
        this.data = data;
        this.jarInfoExtractor = jarInfoExtractor;
    }

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classFileBuffer
    ) throws IllegalClassFormatException {
        Optional.ofNullable(protectionDomain)
                .map(ProtectionDomain::getCodeSource)
                .map(CodeSource::getLocation)
                .map(this.jarInfoExtractor::extractName)
                .ifPresent(this.data::markJarAsUsed);

        return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classFileBuffer);
    }
}
