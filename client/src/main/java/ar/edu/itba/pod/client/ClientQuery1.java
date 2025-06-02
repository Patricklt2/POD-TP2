package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestsNYCRow;
import ar.edu.itba.pod.v1.ComplaintCollator;
import ar.edu.itba.pod.v1.ComplaintMapperNYC;
import ar.edu.itba.pod.v1.ComplaintReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class ClientQuery1 {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery1 Starting ...");

        try {
            // Group Config
            GroupConfig groupConfig = new GroupConfig().setName("l12345").setPassword("l12345-pass");

            // Client Network Config
            ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig();
            clientNetworkConfig.addAddress("127.0.0.1");

            // Client Config
            ClientConfig clientConfig = new ClientConfig().setGroupConfig(groupConfig).setNetworkConfig(clientNetworkConfig);

            // Node Client
            HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);

            MultiMap<Long, RequestsNYCRow> nycRequestsMultiMap = hazelcastInstance.getMultiMap("nycRequests");
            KeyValueSource<Long, RequestsNYCRow> nycRequestsKeyValueSource = KeyValueSource.fromMultiMap(nycRequestsMultiMap);

            // Job Tracker
            JobTracker jobTracker = hazelcastInstance.getJobTracker("complaintsNyc");

            // Text File Reading and Key Value Source Loading
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            AtomicLong auxCounter = new AtomicLong(0);

            try (Stream<String> lines = Files.lines(Paths.get(args[0]), StandardCharsets.UTF_8)) {
                lines.skip(1)
                        .map(line -> line.split(";"))
                        .map(line -> new RequestsNYCRow(
                                Integer.parseInt(line[0]),
                                LocalDateTime.parse(line[1], dateTimeFormatter),
                                line[2],
                                line[3],
                                line[4],
                                line[5],
                                line[6],
                                Double.parseDouble(line[7]),
                                Double.parseDouble(line[8])
                        ))
                        .forEach(nycRequest -> nycRequestsMultiMap.put(auxCounter.getAndIncrement(), nycRequest));
            }

            // MapReduce Job
            Job<Long, RequestsNYCRow> job = jobTracker.newJob(nycRequestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<Complaint, Long>>> future = job
                    .mapper(new ComplaintMapperNYC())
                    .reducer(new ComplaintReducerFactory())
                    .submit(new ComplaintCollator());

            // Wait and retrieve the result
            List<Map.Entry<Complaint, Long>> result = future.get(); //This should be written to a new csv

            result.forEach(System.out::println);
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

}

