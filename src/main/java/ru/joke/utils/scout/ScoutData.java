package ru.joke.utils.scout;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

final class ScoutData {

    private final Map<String, Boolean> jarsUsageInfo;

    ScoutData(final Set<String> initialJars) {
        this.jarsUsageInfo = new ConcurrentHashMap<>(256);
        initialJars.forEach(jarName -> this.jarsUsageInfo.put(jarName, false));
    }

    void markJarAsUsed(final String name) {
        this.jarsUsageInfo.put(name, true);
    }

    Set<String> getUsedJars() {
        return this.jarsUsageInfo.entrySet()
                                    .stream()
                                    .filter(Map.Entry::getValue)
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toCollection(TreeSet::new));
    }

    Set<String> getUnusedJars() {
        return this.jarsUsageInfo.entrySet()
                                    .stream()
                                    .filter(e -> !e.getValue())
                                    .map(Map.Entry::getKey)
                                    .collect(Collectors.toCollection(TreeSet::new));
    }
}
