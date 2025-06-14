package ar.edu.itba.pod.v1.query4;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class StreetAverageReducerFactory implements ReducerFactory<String, String, Long> {
    @Override
    public Reducer<String, Long> newReducer(final String key) {
        return new StreetAverageReducer();
    }

    private static class StreetAverageReducer extends Reducer<String, Long> {
        private final Set<String> types = ConcurrentHashMap.newKeySet();

        @Override
        public void reduce(final String type) {
            types.add(type);
        }

        @Override
        public Long finalizeReduce() {
            return (long) types.size();
        }
    }
}
