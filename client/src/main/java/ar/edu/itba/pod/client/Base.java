package ar.edu.itba.pod.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Base {
    // Runtime.getRuntime().availableProcessors()
    private static final int MAX_THREADS = 8;
    private static final int BATCH_SIZE = 25_000;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Logger logger;

    private static HazelcastInstance hazelcastInstance;

    protected static final Properties env = System.getProperties();

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

    static void requireProperty(String key) {
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
        final String inPath = env.getProperty("inPath");

        final String type = env.getProperty("ev") != null ? "-Short" : "";

        return switch (city.toUpperCase()) {
            case "NYC" -> Paths.get(inPath, "serviceRequestsNYC" + type + ".csv");
            case "CHI" -> Paths.get(inPath, "serviceRequestsCHI" + type + ".csv");
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

    protected static <T> long parseCsv(IMap<Long, T> entries, Function<String[], T> nycLambda,
            BiFunction<String[], Map<String, String>, T> chiLambda) throws IOException, InterruptedException {
        return parseCsv(entries, (n, l) -> n, nycLambda, (n, l) -> n, chiLambda);
    }

    // The triple holy mother of optimization
    protected static <K, V> long parseCsv(IMap<K, V> entries,
            BiFunction<Long, String[], K> nycKey,
            Function<String[], V> nycLambda,
            BiFunction<Long, String[], K> chiKey, // TriFunction doesn't exist natively in Java :/
            BiFunction<String[], Map<String, String>, V> chiLambda) throws IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Base.class);

        final AtomicLong auxCounter = new AtomicLong(0);
        final Map<String, String> srCodeToDescription = new HashMap<>();
        final String city = env.getProperty("city");

        final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        logger.info("Starting csv parse más descriptivo");

        if (city.equalsIgnoreCase("CHI")) {
            try (Stream<String> serviceLines = Files.lines(getTypesFilePath(city), StandardCharsets.UTF_8).skip(1)) {
                serviceLines
                        .map(line -> line.split(";"))
                        .forEach(parts -> srCodeToDescription.put(parts[0], parts[1]));
            }
        }

        logger.debug("Loading file: {}", getRequestsFilePath(city));
        try (Stream<String> lines = Files.lines(getRequestsFilePath(city), StandardCharsets.UTF_8)) {
            final List<String> batch = new ArrayList<>(BATCH_SIZE);
            final Iterator<String> iterator = lines.iterator();

            iterator.next(); // Skip header
            while (iterator.hasNext()) {
                batch.add(iterator.next());

                if (batch.size() == BATCH_SIZE) {
                    executor.submit(new Reader<>(
                            entries,
                            new ArrayList<>(batch),
                            auxCounter,
                            nycKey,
                            nycLambda,
                            chiKey,
                            chiLambda,
                            srCodeToDescription));

                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                executor.submit(new Reader<>(
                        entries,
                        new ArrayList<>(batch),
                        auxCounter,
                        nycKey,
                        nycLambda,
                        chiKey,
                        chiLambda,
                        srCodeToDescription));
            }
        }

        executor.shutdown();
        executor.awaitTermination(4, TimeUnit.HOURS);

        logger.info("End csv parse más descriptivo");

        return auxCounter.get();
    }

    protected static long getTypesCount() throws IOException {
        try (Stream<String> lines = Files.lines(getTypesFilePath(env.getProperty("city")))) {
            return lines.count() - 1; // Subtract 1 for the header
        }
    }

    private static class Reader<K, V> implements Runnable {
        private final IMap<K, V> imap;
        private final List<String> lines;
        private final AtomicLong counter;
        private final BiFunction<Long, String[], K> nycKey;
        private final Function<String[], V> nycValue;
        private final BiFunction<Long, String[], K> chiKey;
        private final BiFunction<String[], Map<String, String>, V> chiValue;
        private final Map<String, String> chicago_codes;

        public Reader(
                final IMap<K, V> imap,
                final List<String> lines,
                final AtomicLong counter,
                final BiFunction<Long, String[], K> nycKey,
                final Function<String[], V> nycLambda,
                final BiFunction<Long, String[], K> chiKey,
                final BiFunction<String[], Map<String, String>, V> chiLambda,
                final Map<String, String> srCodeToDescription) {
            this.imap = imap;
            this.lines = lines;
            this.counter = counter;
            this.nycKey = nycKey;
            this.nycValue = nycLambda;
            this.chiKey = chiKey;
            this.chiValue = chiLambda;
            this.chicago_codes = srCodeToDescription;
        }

        @Override
        public void run() {
            final Map<K, V> out = new HashMap<>(lines.size() + 1, 1.0f);

            final Boolean nyc = env.getProperty("city").equalsIgnoreCase("NYC");
            final BiFunction<Long, String[], K> key = nyc ? nycKey : chiKey;
            final Function<String[], V> lambda = nyc ? nycValue : (l) -> chiValue.apply(l, chicago_codes);

            for (String line : lines) {
                final String[] data = line.split(";");

                final V value = lambda.apply(data);
                if (value == null) {
                    continue;
                }

                out.put(key.apply(counter.getAndIncrement(), data), value);
            }

            imap.putAll(out);
        }
    }
}
