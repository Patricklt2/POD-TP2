package ar.edu.itba.pod.v0Generics;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class GenericCountReducerFactory<K> implements ReducerFactory<K, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(K key) {
        return new GenericCountReducerFactory.GenericCountReducer();
    }

    private static class GenericCountReducer extends Reducer<Long, Long> {
        private Long sum = 0L;

        @Override
        public void reduce(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            return sum;
        }
    }
}
