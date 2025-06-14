package ar.edu.itba.pod.v1.query5;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class ResolutionsCombinerFactory implements CombinerFactory<Resolutions, Long, Long> {
    @Override
    public Combiner<Long, Long> newCombiner(Resolutions key) {
        return new ar.edu.itba.pod.v1.query5.ResolutionsCombinerFactory.ResolutionsCombiner();
    }

    private static class ResolutionsCombiner extends Combiner<Long, Long> {
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
