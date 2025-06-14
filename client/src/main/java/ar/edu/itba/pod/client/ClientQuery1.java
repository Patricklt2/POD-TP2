package ar.edu.itba.pod.client;

import ar.edu.itba.pod.v0Mappers.GenericCountMapper;
import ar.edu.itba.pod.v1.query1.ComplaintCollator;
import ar.edu.itba.pod.v1.query1.ComplaintCombinerFactory;
import ar.edu.itba.pod.v1.query1.ComplaintMapper;
import ar.edu.itba.pod.v1.query1.ComplaintReducerFactory;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ClientQuery1 extends Base {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public ClientQuery1() {
        super(logger);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery1 Starting ...");
        new ClientQuery1().run();

        try {
            HazelcastInstance hz = getHazelcastInstance();
            IMap<Long, String> requestsIMap = hz.getMap("requests");
            KeyValueSource<Long, String> requestsKeyValueSource = KeyValueSource.fromMap(requestsIMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("complaints");

            parseCsv(requestsIMap, line -> new StringBuilder().append(line[3]).append(';').append(line[2]).toString(),
                    (line, srCodeToDescription) -> new StringBuilder().append(srCodeToDescription.get(line[1])).append(';').append(line[2]).toString(),
                    p -> true);

            // MapReduce Job
            Job<Long, String> job = jobTracker.newJob(requestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<String, Long>>> future = job
                    .mapper(new GenericCountMapper<>())
                    .combiner(new ComplaintCombinerFactory())
                    .reducer(new ComplaintReducerFactory())
                    .submit(new ComplaintCollator());

            writeOutputToCsvQuery1(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery1(List<Map.Entry<String, Long>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(env.getProperty("outPath"), "query1" + env.getProperty("city") + "Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("type;agency;requests");
            writer.newLine();

            for (Map.Entry<String, Long> entry : result) {
                Long requests = entry.getValue();

                String line = String.format("%s;%d",entry.getKey(), requests);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error while writing output for query1",e);
        }
    }
}
