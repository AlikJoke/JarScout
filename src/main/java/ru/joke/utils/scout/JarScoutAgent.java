package ru.joke.utils.scout;

import java.lang.instrument.Instrumentation;
import java.util.Set;

public final class JarScoutAgent {

    public static void premain(final String args, final Instrumentation instrumentation) {
        agentmain(args, instrumentation);
    }

    public static void agentmain(final String args, final Instrumentation instrumentation) {
        final var jarNameExtractor = new JarInfoExtractor();
        final var jarCollector = new JarCollector(jarNameExtractor);
        final var config = ScoutConfiguration.parse(args);

        final var jars = collectJars(jarCollector, config);
        final var data = new ScoutData(jars);

        final var transformer = new JarScoutTransformer(data, jarNameExtractor);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final var outputWriter = createOutputWriter(config);
            outputWriter.write(data);
        }));

        instrumentation.addTransformer(transformer);
    }

    private static Set<String> collectJars(
            final JarCollector jarCollector,
            final ScoutConfiguration configuration
    ) {
        try {
            return jarCollector.collect(configuration.targetArtifactPath());
        } catch (RuntimeException ex) {
            final var outputWriter = createOutputWriter(configuration);
            outputWriter.write(ex);

            throw ex;
        }
    }

    private static ScoutDataOutputWriter createOutputWriter(final ScoutConfiguration configuration) {
        return new ScoutDataOutputWriter(configuration);
    }
}
