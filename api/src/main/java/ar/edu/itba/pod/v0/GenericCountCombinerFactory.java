package ar.edu.itba.pod.v0;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class GenericCountCombinerFactory<K> implements CombinerFactory<K, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(final K key) {
        return new GenericCountCombinerFactory.CountCombiner();
    }

    private static class CountCombiner extends Combiner<Long, Long> {
        private long sum = 0L;

        @Override
        public void reset() {
            sum = 0L;
        }

        @Override
        public void combine(final Long value) {
            sum += value;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }
    }
}
