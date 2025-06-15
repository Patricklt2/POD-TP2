package ar.edu.itba.pod.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Fake client for the reader speed test.
 */
public class ClientQuery0 extends Base {
    private static final Logger logger = LoggerFactory.getLogger(ClientQuery1.class);

    public ClientQuery0() {
        super(logger);
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        logger.info("ClientQuery0 Starting ...");
        new ClientQuery0().run();

        try {
            final HazelcastInstance hz = getHazelcastInstance();
            final IMap<Long, Boolean> map = hz.getMap("g1-reader-speed-test");
            final long count = parseCsv(map, line -> true, (line, sr) -> true);
            logger.info("Total records processed: {}", count);
        } finally {
            HazelcastClient.shutdownAll();
        }
    }
}
