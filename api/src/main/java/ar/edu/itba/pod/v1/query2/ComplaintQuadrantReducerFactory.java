package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.NeighborhoodQuadrant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantReducerFactory implements ReducerFactory<NeighborhoodQuadrant, String, String> {
    @Override
    public Reducer<String, String> newReducer(final NeighborhoodQuadrant key) {
        return new PostComplaintQuadrantReducer();
    }

    private static class PostComplaintQuadrantReducer extends Reducer<String, String> {
        final private Map<String, AtomicLong> counts = new ConcurrentHashMap<>();

        @Override
        public void reduce(final String value) {
            counts.computeIfAbsent(value, k -> new AtomicLong(0)).incrementAndGet();
        }

        @Override
        public String finalizeReduce() {
            final Iterator<Map.Entry<String, AtomicLong>> it = counts.entrySet().iterator();

            if (!it.hasNext()) {
                return null;
            }

            final Map.Entry<String, AtomicLong> first = it.next();

            String maxType = first.getKey();
            long maxCount = first.getValue().get();

            while (it.hasNext()) {
                final Map.Entry<String, AtomicLong> entry = it.next();
                final long count = entry.getValue().get();

                if (count > maxCount || (count == maxCount && entry.getKey().compareTo(maxType) < 0)) {
                    maxCount = count;
                    maxType = entry.getKey();
                }
            }

            return maxType;
        }
    }

    @SuppressWarnings("unused")
    private static class PreComplaintQuadrantReducer extends Reducer<String, String> {
        final private Map<String, Long> counts = new HashMap<>();
        final private Lock lock = new ReentrantLock();

        private long max = 0;
        private String winner = null;

        @Override
        public void reduce(final String value) {
            lock.lock();
            try {
                final long count = counts.merge(value, 1L, Long::sum);

                if (count > max || (count == max && (value.compareTo(winner) < 0))) {
                    max = count;
                    winner = value;
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public String finalizeReduce() {
            return winner;
        }
    }
}
