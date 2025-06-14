package ar.edu.itba.pod.v0Generics;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class GenericCountCombinerFactory<K> implements CombinerFactory<K, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(K key) {
        return new GenericCountCombinerFactory.CountCombiner();
    }

    private static class CountCombiner extends Combiner<Long, Long> {
        private Long sum = 0L;

        @Override
        public void reset() {
            sum = 0L;
        }

        @Override
        public void combine(Long value) {
            sum += value;
        }

        @Override
        public Long finalizeChunk() {
            return sum;
        }
    }
}
