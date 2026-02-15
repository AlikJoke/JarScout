package ru.joke.utils.scout;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

final class ScoutDataOutputWriter {

    private final ScoutConfiguration configuration;

    ScoutDataOutputWriter(final ScoutConfiguration configuration) {
        this.configuration = configuration;
    }

    void write(final Exception exception) {
        final var pw = createOutputStream();
        try (pw) {
            writeConfiguration(pw);
            exception.printStackTrace(pw);
        }
    }

    void write(final ScoutData data) {
        final var pw = createOutputStream();

        try (pw) {
            writeConfiguration(pw);

            pw.println("#Unused#");
            pw.println(formatOutputData(data.getUnusedJars()));

            pw.println();

            pw.println("#Used#");
            pw.println(formatOutputData(data.getUsedJars()));
        }
    }

    private void writeConfiguration(final PrintStream pw) {
        pw.println("#Parameters#");
        pw.println(this.configuration);

        pw.println();
    }

    private String formatOutputData(final Set<String> data) {
        return data
                .stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private PrintStream createOutputStream() {
        try {
            return this.configuration.outputFilePath() != null
                    ? new PrintStream(this.configuration.outputFilePath(), StandardCharsets.UTF_8)
                    : System.out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
