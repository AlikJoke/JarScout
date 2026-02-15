package ru.joke.utils.scout;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

final class ScoutConfiguration {

    private static final String OUTPUT_FILE_PATH = "output.file.path";
    private static final String TARGET_ARTIFACT_PATH = "artifact.path";

    private final String outputFilePath;
    private final String targetArtifactPath;

    private ScoutConfiguration(
            final String outputFilePath,
            final String targetArtifactPath
    ) {
        this.outputFilePath = outputFilePath;
        this.targetArtifactPath = targetArtifactPath;
    }

    String outputFilePath() {
        return this.outputFilePath;
    }

    String targetArtifactPath() {
        return this.targetArtifactPath;
    }

    @Override
    public String toString() {
        return "{" + OUTPUT_FILE_PATH + "='" + outputFilePath + '\'' + "," + TARGET_ARTIFACT_PATH + "='" + targetArtifactPath + '\'' + '}';
    }

    static ScoutConfiguration parse(final String args) {
        final Map<String, String> argsMap = args == null ? Collections.emptyMap() : parseArgs(args);
        return new ScoutConfiguration(
                argsMap.get(OUTPUT_FILE_PATH),
                argsMap.get(TARGET_ARTIFACT_PATH)
        );
    }

    private static Map<String, String> parseArgs(final String args) {
        return Arrays.stream(args.split(","))
                        .map(arg -> arg.split("="))
                        .filter(arg -> arg.length > 1 && !arg[1].isEmpty())
                        .collect(Collectors.toMap(arg -> arg[0].toLowerCase(), arg -> arg[1]));
    }
}
