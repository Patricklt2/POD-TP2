package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.GeoLocation;
import ar.edu.itba.pod.v1.query1.ComplaintCollator;
import ar.edu.itba.pod.v1.query1.ComplaintMapper;
import ar.edu.itba.pod.v1.query1.ComplaintReducerFactory;
import ar.edu.itba.pod.v1.query2.GeoLocationCollator;
import ar.edu.itba.pod.v1.query2.GeoLocationMapper;
import ar.edu.itba.pod.v1.query2.GeoLocationReducerFactory;
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
            MultiMap<Long, GeoLocation> requestsMultiMap = hz.getMultiMap("requests");
            KeyValueSource<Long, GeoLocation> requestsKeyValueSource = KeyValueSource.fromMultiMap(requestsMultiMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("complaints");

            parseCsv(requestsMultiMap, line -> new GeoLocation(Double.parseDouble(line[7]), Double.parseDouble(line[8])),
                    (line, srCodeToDescription) -> new GeoLocation(Double.parseDouble(srCodeToDescription.get(line[10])), Double.parseDouble(line[11])));

            // MapReduce Job
            Job<Long, GeoLocation> job = jobTracker.newJob(requestsKeyValueSource);
            ICompletableFuture<List<Map.Entry<GeoLocation, Long>>> future = job
                    .mapper(new GeoLocationMapper())
                    .reducer(new GeoLocationReducerFactory())
                    .submit(new GeoLocationCollator());

            writeOutputToCsvQuery2(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery2(List<Map.Entry<GeoLocation, Long>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(env.getProperty("outPath"), "query1Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("type;agency;requests");
            writer.newLine();

            for (Map.Entry<GeoLocation, Long> entry : result) {
                /*GeoLocation complaint = entry.getKey();
                Long requests = entry.getValue();

                String line = String.format("%s;%s;%d",
                        complaint.getType(),
                        complaint.getAgency(),
                        requests);
                writer.write(line);
                writer.newLine();
                 */
            }
        } catch (IOException e) {
            logger.error("Error while writing output for query2",e);
        }
    }
}
