package com.coindesk.demo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class CurrencyMappingLoader {

    private final ResourceLoader resourceLoader;
    private final String location;
    private final ObjectMapper mapper = new ObjectMapper();

    private volatile Map<String, String> cache = Collections.emptyMap();

    public CurrencyMappingLoader(ResourceLoader resourceLoader,
                                 @Value("${currency.mapping.file:classpath:currencies_zh-TW.json}") String location)
            throws IOException {
        this.resourceLoader = resourceLoader;
        this.location = location;
        loadFile();
        watchFileChange();
    }

    public String getDisplayName(String code) {
        if (code == null) return null;
        return cache.getOrDefault(code.toUpperCase(), code);
    }

    private synchronized void loadFile() throws IOException {
        Resource resource = resourceLoader.getResource(location);
        if (!resource.exists()) {
            cache = Collections.emptyMap();
            System.out.println("[CurrencyMappingLoader] Resource not found: " + location);
            return;
        }
        try (InputStream in = resource.getInputStream()) {
            Map<String, String> data = mapper.readValue(in, new TypeReference<Map<String, String>>() {
            });
            Map<String, String> upperCaseMap = new HashMap<>(Math.max(16, data.size()));
            data.forEach((k, v) -> {
                if (k != null && v != null) {
                    upperCaseMap.put(k.toUpperCase(), v);
                }
            });
            cache = upperCaseMap;
            System.out.println("[CurrencyMappingLoader] Mapping reloaded: " + cache.size() + " entries from " + location);
        }
    }

    private void watchFileChange() {
        final Resource resource = resourceLoader.getResource(location);
        final boolean watchable;
        final Path watchFilePath;
        try {
            watchable = resource.exists() && resource.isFile();
            watchFilePath = watchable ? resource.getFile().toPath() : null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (!watchable || watchFilePath == null) {
            System.out.println("[CurrencyMappingLoader] Watch disabled: " + location);
            return;
        }

        Thread t = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Path dir = watchFilePath.getParent();
                if (dir == null) return;

                dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
                Path targetFileName = watchFilePath.getFileName();

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) continue;

                        Path changed = (Path) event.context();
                        if (changed != null && changed.getFileName().equals(targetFileName)) {
                            try {
                                loadFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) break;
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void reload() throws IOException {
        loadFile();
    }
}
