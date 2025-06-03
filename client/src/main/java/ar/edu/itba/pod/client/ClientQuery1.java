package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestRow;
import ar.edu.itba.pod.common.RequestsCHIRow;
import ar.edu.itba.pod.common.RequestsNYCRow;
import ar.edu.itba.pod.v1.ComplaintCollator;
import ar.edu.itba.pod.v1.ComplaintMapper;
import ar.edu.itba.pod.v1.ComplaintReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.MultiMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class ClientQuery1 extends Base {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public ClientQuery1() {
        super(logger);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery1 Starting ...");
        new ar.edu.itba.pod.client.ClientQuery1().run();

        try {
            HazelcastInstance hz = getHazelcastInstance();
            MultiMap<Long, RequestRow> requestsMultiMap = hz.getMultiMap("requests");
            KeyValueSource<Long, RequestRow> requestsKeyValueSource = KeyValueSource.fromMultiMap(requestsMultiMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("complaints");

            // Text File Reading and Key Value Source Loading
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            AtomicLong auxCounter = new AtomicLong(0);

            String city = env.getProperty("city");
            try (Stream<String> lines = Files.lines(getRequestsFilePath(city), StandardCharsets.UTF_8)) {
                if (city.equals("NYC")) {
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
                                    Double.parseDouble(line[8])))
                            .forEach(nycRequest -> requestsMultiMap.put(auxCounter.getAndIncrement(), nycRequest));
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

                    lines.skip(1)
                            .map(line -> line.split(";"))
                            .map(line -> new RequestsCHIRow(
                                    line[0],
                                    srCodeToDescription.get(line[1]),
                                    line[3],
                                    line[4],
                                    LocalDateTime.parse(line[5], dateTimeFormatter),
                                    Integer.parseInt(line[6]),
                                    line[7].charAt(0),
                                    line[4],
                                    line[5],
                                    line[6],
                                    Double.parseDouble(line[7]),
                                    Double.parseDouble(line[8])))
                            .forEach(nycRequest -> requestsMultiMap.put(auxCounter.getAndIncrement(), nycRequest));
                }
            }

            // MapReduce Job
            Job<Long, RequestRow> job = jobTracker.newJob(requestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<Complaint, Long>>> future = job
                    .mapper(new ComplaintMapper())
                    .reducer(new ComplaintReducerFactory())
                    .submit(new ComplaintCollator());

            writeOutputToCsvQuery1(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery1(List<Map.Entry<Complaint, Long>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(env.getProperty("outPath"), "query1Output.csv"))) {
            writer.write("type;agency;requests");
            writer.newLine();

            for (Map.Entry<Complaint, Long> entry : result) {
                Complaint complaint = entry.getKey();
                Long requests = entry.getValue();

                String line = String.format("%s;%s;%d",
                        complaint.getType(),
                        complaint.getAgency(),
                        requests);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
