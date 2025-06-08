package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Address;
import ar.edu.itba.pod.common.AddressEvent;
import ar.edu.itba.pod.common.AddressEventKey;
import ar.edu.itba.pod.v1.query4.StreetAverageCollator;
import ar.edu.itba.pod.v1.query4.StreetAverageKeyPredicate;
import ar.edu.itba.pod.v1.query4.StreetAverageMapper;
import ar.edu.itba.pod.v1.query4.StreetAverageReducerFactory;
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
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ClientQuery4 extends Base {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery4.class);

    public ClientQuery4() {
        super(logger);
    }

    protected void run() throws InterruptedException {
        requireProperty("neighbourhood");
        super.run();
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery4 Starting ...");
        new ClientQuery4().run();

        try {
            final HazelcastInstance hz = getHazelcastInstance();

            final IMap<AddressEventKey, AddressEvent> entries = hz.getMap("addresses");
            final KeyValueSource<AddressEventKey, AddressEvent> source = KeyValueSource.fromMap(entries);

            final String neighbourhood = env.getProperty("neighbourhood").replaceAll("_", " ");

            final JobTracker tracker = hz.getJobTracker("averages");

            parseCsv(entries,
                    (n, line) -> new AddressEventKey(n, line[6]),
                    line -> new AddressEvent(new Address(line[4]), line[3]),
                    (n, line) -> new AddressEventKey(n, line[9]),
                    (line, sr) -> new AddressEvent(new Address(line[6].charAt(0), line[7], line[8]), sr.get(line[1])));

            final Job<AddressEventKey, AddressEvent> job = tracker.newJob(source);

            final ICompletableFuture<List<Map.Entry<String, Long>>> future = job
                    .keyPredicate(new StreetAverageKeyPredicate(neighbourhood))
                    .mapper(new StreetAverageMapper())
                    .reducer(new StreetAverageReducerFactory())
                    .submit(new StreetAverageCollator());

            writeOutputToCsvQuery4(future.get());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery4(List<Map.Entry<String, Long>> result) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(Paths.get(env.getProperty("outPath"), "query4Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("street;typePercentage\n");

            final double percenter = 100 / getTypesCount();

            final DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.DOWN);

            for (Map.Entry<String, Long> entry : result) {
                final String address = entry.getKey();
                final double percent = entry.getValue() * percenter;

                writer.write("%s;%s%%\n".formatted(address, df.format(percent)));
            }
        } catch (IOException e) {
            logger.error("Error while writing output for query4", e);
        }
    }
}
