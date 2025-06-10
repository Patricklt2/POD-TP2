package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.NeighborhoodQuadrant;

import java.util.Iterator;
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

        @Override
        public void reduce(String value) {
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
}
