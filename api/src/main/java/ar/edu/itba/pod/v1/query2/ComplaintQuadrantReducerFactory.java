package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.NeighborhoodQuadrant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantReducerFactory implements ReducerFactory<NeighborhoodQuadrant, String, String> {
    @Override
    public Reducer<String, String> newReducer(NeighborhoodQuadrant key) {
        return new ComplaintQuadrantReducer();
    }

    private static class ComplaintQuadrantReducer extends Reducer<String, String> {
        final private Map<String, AtomicLong> counts = new ConcurrentHashMap<>();

        private ReduceEntry max = null;

        @Override
        public synchronized void reduce(String value) {
            final long count = counts.computeIfAbsent(value, k -> new AtomicLong(0)).incrementAndGet();

            if (max == null || count > max.count) {
                max = new ReduceEntry(value, count);
            }
        }

        @Override
        public String finalizeReduce() {
            return max.type;
        }

        private static record ReduceEntry(String type, long count) {
        }
    }
}
