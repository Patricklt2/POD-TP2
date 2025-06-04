package ar.edu.itba.pod.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Base {

    protected static final Properties env = System.getProperties();
    private static HazelcastInstance hazelcastInstance;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Logger logger;

    public Base(Logger logger) {
        this.logger = logger;
    }

    protected void run() throws InterruptedException {
        logger.info("POD-TP2 Base Client Starting...");

        // Base Args validation
        requireProperty("inPath");
        requireProperty("outPath");
        requireProperty("city");
        requireProperty("addresses");

        List<String> addresses = Arrays.stream(env.get("addresses").toString().split(";")).toList();

        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("-Daddresses is required and cannot be empty");
        }

        // Group Config
        GroupConfig groupConfig = new GroupConfig().setName("g1-tp2-1234").setPassword("g1-tp2-1234-pass");

        // Client Network Config
        ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();

        for (String address : addresses) {
            clientNetworkConfig.addAddress(address);
        }

        // Client Config
        ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig)
                .setNetworkConfig(clientNetworkConfig);

        // Node Client
        hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
    }

    private void requireProperty(String key) {
        String value = env.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(String.format("-D%s is required and cannot be empty", key));
        }
    }

    protected static HazelcastInstance getHazelcastInstance() {
        if (hazelcastInstance == null) {
            throw new IllegalStateException("HazelcastInstance not initialized. Call run() first.");
        }
        return hazelcastInstance;
    }

    protected static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    protected static Path getRequestsFilePath(String city) {
        String inPath = env.getProperty("inPath");

        return switch (city.toUpperCase()) {
            case "NYC" -> Paths.get(inPath, "serviceRequestsNYC-Short.csv");
            case "CHI" -> Paths.get(inPath, "serviceRequestsCHI-Short.csv");
            default -> throw new IllegalArgumentException("Invalid city: must be NYC or CHI");
        };
    }

    protected static Path getTypesFilePath(String city) {
        String inPath = env.getProperty("inPath");

        return switch (city.toUpperCase()) {
            case "NYC" -> Paths.get(inPath, "serviceTypesNYC.csv");
            case "CHI" -> Paths.get(inPath, "serviceTypesCHI.csv");
            default -> throw new IllegalArgumentException("Invalid city: must be NYC or CHI");
        };
    }

    //Holy mother of optimizations
    protected static <T> void parseCsv(MultiMap<Long, T> entries, Function<String[], T> nycLambda,
                                       BiFunction<String[], Map<String, String>, T> chiLambda) throws IOException {

        AtomicLong auxCounter = new AtomicLong(0);

        String city = env.getProperty("city");
        try (Stream<String> lines = Files.lines(getRequestsFilePath(city), StandardCharsets.UTF_8)) {
            if (city.equals("NYC")) {
                lines.skip(1)
                        .map(line -> line.split(";"))
                        .map(nycLambda)
                        .forEach(nycRequest -> entries.put(auxCounter.getAndIncrement(), nycRequest));
            } else {
                //This can most certainly be optimized
                Map<String, String> srCodeToDescription = new HashMap<>();

                try (Stream<String> serviceLines = Files.lines(getTypesFilePath(city), StandardCharsets.UTF_8)){
                    serviceLines.skip(1)
                            .map(line -> line.split(";"))
                            .forEach(parts -> srCodeToDescription.put(parts[0], parts[1]));
                }
                //This can most certainly be optimized
                // Should probably add another map reduce! But maybe we could keep that for v2 ;)
                //its just 86 lines.... maybe not needed?
                // Could use a boolean state so it does not load into map unless needed :)

                lines.skip(1)
                        .map(line -> line.split(";"))
                        .map(line -> chiLambda.apply(line, srCodeToDescription))
                        .forEach(nycRequest -> entries.put(auxCounter.getAndIncrement(), nycRequest));
            }
        }
    }
}
