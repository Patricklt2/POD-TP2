package ar.edu.itba.pod.v1.query1;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class ComplaintCombinerFactory implements CombinerFactory<String, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(String key) {
        return new ComplaintCombiner();
    }

    private static class ComplaintCombiner extends Combiner<Long, Long> {
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
