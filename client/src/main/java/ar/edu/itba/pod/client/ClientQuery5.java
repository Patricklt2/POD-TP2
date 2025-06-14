package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Resolutions;
import ar.edu.itba.pod.v0.GenericCountCombinerFactory;
import ar.edu.itba.pod.v0.GenericCountMapper;
import ar.edu.itba.pod.v0.GenericCountReducerFactory;
import ar.edu.itba.pod.v1.query5.ResolutionsCollator;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ClientQuery5 extends Base {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery5.class);

    public ClientQuery5() {
        super(logger);
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery5 Starting ...");
        new ClientQuery5().run();

        try {
            HazelcastInstance hz = getHazelcastInstance();
            IMap<Long, Resolutions> requestsIMap = hz.getMap("requestsYTD");
            KeyValueSource<Long, Resolutions> requestsKeyValueSource = KeyValueSource.fromMap(requestsIMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("solvedYTD");

            parseCsv(requestsIMap,
                    line -> line[5].equals("Closed")
                            ? new Resolutions(line[2], LocalDateTime.parse(line[1], getDateTimeFormatter()))
                            : null,
                    (line, unused) -> line[3].equals("Completed")
                            ? new Resolutions(line[2], LocalDateTime.parse(line[4], getDateTimeFormatter()))
                            : null);

            // MapReduce Job
            Job<Long, Resolutions> job = jobTracker.newJob(requestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<Resolutions, Long>>> future = job
                    .mapper(new GenericCountMapper<>())
                    .combiner(new GenericCountCombinerFactory<>())
                    .reducer(new GenericCountReducerFactory<>())
                    .submit(new ResolutionsCollator());

            writeOutputToCsvQuery5(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery5(List<Map.Entry<Resolutions, Long>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(
                        Paths.get(env.getProperty("outPath"), "query5" + env.getProperty("city") + "Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("agency;year;month;resolvedYTD");
            writer.newLine();

            for (Map.Entry<Resolutions, Long> entry : result) {
                Resolutions resolutions = entry.getKey();
                Long solved = entry.getValue();

                String line = String.format("%s;%d;%d;%d",
                        resolutions.getAgency(),
                        resolutions.getYear(),
                        resolutions.getMonth(),
                        solved);

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            logger.error("Error while writing output for query 5", e);
        }
    }
}

// [A,Y,D] --> [A,Y,D] [C] --> <[A, Y, D], [SUM]>