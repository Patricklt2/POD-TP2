package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.ComplaintQuadrant;
import ar.edu.itba.pod.common.NeighborhoodQuadrant;
import ar.edu.itba.pod.v1.query2.ComplaintQuadrantCollator;
import ar.edu.itba.pod.v1.query2.ComplaintQuadrantMapper;
import ar.edu.itba.pod.v1.query2.ComplaintQuadrantReducerFactory;
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

public class ClientQuery2 extends Base {

    private static final Logger logger = LoggerFactory.getLogger(ClientQuery2.class);

    public ClientQuery2() {
        super(logger);
    }

    protected void run() throws InterruptedException {
        requireProperty("q");
        super.run();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery2 Starting ...");
        new ClientQuery2().run();

        try {
            HazelcastInstance hz = getHazelcastInstance();
            IMap<Long, ComplaintQuadrant> requestsIMap = hz.getMap("requests");
            KeyValueSource<Long, ComplaintQuadrant> requestsKeyValueSource = KeyValueSource.fromMap(requestsIMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("complaints");

            final Double q = getQ();
            parseCsv(requestsIMap,
                    line -> new ComplaintQuadrant(line[3], line[6],
                            (long) (Double.parseDouble(line[7]) / q),
                            (long) (Double.parseDouble(line[8]) / q)),
                    (line, sr) -> new ComplaintQuadrant(sr.get(line[1]), line[9],
                            (long) (Double.parseDouble(line[10]) / q),
                            (long) (Double.parseDouble(line[11]) / q)));

            // MapReduce Job
            Job<Long, ComplaintQuadrant> job = jobTracker.newJob(requestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<NeighborhoodQuadrant, String>>> future = job
                    .mapper(new ComplaintQuadrantMapper())
                    .reducer(new ComplaintQuadrantReducerFactory())
                    .submit(new ComplaintQuadrantCollator());

            writeOutputToCsvQuery2(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery2(List<Map.Entry<NeighborhoodQuadrant, String>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(
                        Paths.get(env.getProperty("outPath"), "query2" + env.getProperty("city") + "Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("neighbourhood;quadLat;quadLon;topType");
            writer.newLine();

            for (Map.Entry<NeighborhoodQuadrant, String> entry : result) {
                final NeighborhoodQuadrant q = entry.getKey();
                final String type = entry.getValue();

                final String line = String.format("%s;%d;%d;%s",
                        q.getNeighborhood(),
                        Math.round(q.getLocation().getLatitude()),
                        Math.round(q.getLocation().getLongitude()),
                        type);

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Error while writing output for query2", e);
        }
    }

    protected static Double getQ() {
        return Double.parseDouble(env.getProperty("q"));
    }
}
