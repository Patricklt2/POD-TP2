package ar.edu.itba.pod.v1.query1;

import ar.edu.itba.pod.common.Complaint;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class ComplaintCombinerFactory implements CombinerFactory<Complaint, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(Complaint key) {
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
