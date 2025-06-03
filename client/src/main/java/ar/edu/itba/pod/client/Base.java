package ar.edu.itba.pod.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class Base {

    protected static final Properties env = System.getProperties();
    protected static HazelcastInstance hazelcastInstance;
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

    protected static Path getRequestsFilePath(String city) {
        String inPath = env.getProperty("inPath");

        return switch (city.toUpperCase()) {
            case "NYC" -> Paths.get(inPath, "serviceRequestsNYC.csv");
            case "CHI" -> Paths.get(inPath, "serviceRequestsCHI.csv");
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
}
