package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Resolutions;
import ar.edu.itba.pod.v0.GenericCountMapper;
import ar.edu.itba.pod.v1.query3.MovingAverageReducerFactory;
import ar.edu.itba.pod.v1.query3.MovingAverageCollator;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

public class ClientQuery3 extends Base {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery3.class);

    public ClientQuery3() {
        super(logger);
    }

    @Override
    protected void run() throws InterruptedException {
        requireProperty("w");
        super.run();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        logger.info("ClientQuery3 Starting ...");
        new ClientQuery3().run();

        try {
            HazelcastInstance hz = getHazelcastInstance();
            IMap<Long, Resolutions> requestsIMap = hz.getMap("requestsMovingAvg");
            KeyValueSource<Long, Resolutions> source = KeyValueSource.fromMap(requestsIMap);
            JobTracker tracker = hz.getJobTracker("movingAvg");

            parseCsv(
                    requestsIMap,
                    line -> !line[5].equals("Closed")
                            ? new Resolutions(line[2], LocalDateTime.parse(line[1], getDateTimeFormatter()))
                            : null,
                    (line, unused) -> line[3].equals("Open")
                            ? new Resolutions(line[2], LocalDateTime.parse(line[4], getDateTimeFormatter()))
                            : null);

            // MapReduce
            Job<Long, Resolutions> job = tracker.newJob(source);
            ICompletableFuture<List<String>> future = job
                    .mapper(new GenericCountMapper<>())
                    .reducer(new MovingAverageReducerFactory())
                    .submit(new MovingAverageCollator(Integer.parseInt(env.getProperty("w"))));

            writeOutputToCsvQuery3(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery3(List<String> result) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(env.getProperty("outPath"), "query3" + env.getProperty("city") + "Output.csv"),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            writer.write("agency;year;month;movingAvg");
            writer.newLine();

            for (String line : result) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error writing output for query 3", e);
        }
    }
}
