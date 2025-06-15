package ar.edu.itba.pod.client;

import ar.edu.itba.pod.common.Address;
import ar.edu.itba.pod.common.AddressEvent;
import ar.edu.itba.pod.common.HeuristicNeighbourhood;
import ar.edu.itba.pod.v1.query4.StreetAverageCollator;
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
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
            final String city = env.getProperty("city");
            final HeuristicNeighbourhood neighbourhood;

            try {
                neighbourhood = HeuristicNeighbourhood.valueOf(env.getProperty("neighbourhood"));
            } catch (IllegalArgumentException e) {
                logger.warn("The neighbourhood {} does not exist", env.getProperty("neighbourhood"), city);
                writeOutputToCsvQuery4(List.of(), 1);
                return;
            }

            if (!neighbourhood.getCity().equals(city)) {
                logger.warn("The neighbourhood {} does not match the city {}", neighbourhood, city);
                writeOutputToCsvQuery4(List.of(), 1);
                return;
            }

            final HazelcastInstance hz = getHazelcastInstance();

            final IMap<Long, AddressEvent> entries = hz.getMap("g1-addresses");
            final KeyValueSource<Long, AddressEvent> source = KeyValueSource.fromMap(entries);

            final JobTracker tracker = hz.getJobTracker("g1-averages");

            final int maxTypesCount = (int) getTypesCount();
            final Set<String> types = ConcurrentHashMap.newKeySet(maxTypesCount + 1);

            parseCsv(entries,
                    line -> {
                        if (types.size() < maxTypesCount) {
                            types.add(line[3]);
                        }

                        return neighbourhood.hequals(line[6]) ? new AddressEvent(new Address(line[4]), line[3]) : null;
                    },
                    (line, sr) -> {
                        if (types.size() < maxTypesCount) {
                            types.add(line[1]);
                        }

                        return neighbourhood.hequals(line[9])
                                ? new AddressEvent(new Address(line[6].charAt(0), line[7], line[8]), sr.get(line[1]))
                                : null;
                    });

            final Job<Long, AddressEvent> job = tracker.newJob(source);

            final ICompletableFuture<List<Map.Entry<String, Long>>> future = job
                    .mapper(new StreetAverageMapper())
                    .reducer(new StreetAverageReducerFactory())
                    .submit(new StreetAverageCollator());

            writeOutputToCsvQuery4(future.get(), types.size());
        } finally {
            HazelcastClient.shutdownAll();
        }
    }

    private static void writeOutputToCsvQuery4(List<Map.Entry<String, Long>> result, int typesCount) {
        try (BufferedWriter writer = Files
                .newBufferedWriter(
                        Paths.get(env.getProperty("outPath"), "query4" + env.getProperty("city") + "Output.csv"),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
            writer.write("street;typePercentage\n");

            final double percenter = (double) 100 / Math.max(typesCount, 1);

            final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setDecimalSeparator('.');
            final DecimalFormat df = new DecimalFormat("#.##", symbols);
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
