package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.v1.query1.ComplaintCollator;
import ar.edu.itba.pod.v1.query1.ComplaintMapper;
import ar.edu.itba.pod.v1.query1.ComplaintReducerFactory;
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
            MultiMap<Long, Complaint> requestsMultiMap = hz.getMultiMap("requests");
            KeyValueSource<Long, Complaint> requestsKeyValueSource = KeyValueSource.fromMultiMap(requestsMultiMap);

            // Job Tracker
            JobTracker jobTracker = hz.getJobTracker("complaints");

            parseCsv(requestsMultiMap, line -> new Complaint(line[3], line[2]),
                    (line, srCodeToDescription) -> new Complaint(srCodeToDescription.get(line[1]), line[2]));

            // MapReduce Job
            Job<Long, Complaint> job = jobTracker.newJob(requestsKeyValueSource);
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
            logger.error("Error while writing output for query1",e);
        }
    }
}
