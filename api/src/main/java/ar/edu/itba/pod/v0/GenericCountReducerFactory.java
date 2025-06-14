package ar.edu.itba.pod.v0;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class GenericCountReducerFactory<K> implements ReducerFactory<K, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(final K key) {
        return new GenericCountReducerFactory.GenericCountReducer();
    }

    private static class GenericCountReducer extends Reducer<Long, Long> {
        private long sum = 0L;

        @Override
        public void reduce(final Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
