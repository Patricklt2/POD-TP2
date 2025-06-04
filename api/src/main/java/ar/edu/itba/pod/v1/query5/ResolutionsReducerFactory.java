package ar.edu.itba.pod.v1.query5;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ResolutionsReducerFactory implements ReducerFactory<Resolutions, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(Resolutions key) {
        return new ResolutionsReducerFactory.ResolutionsReducer();
    }

    private static class ResolutionsReducer extends Reducer<Long, Long> {
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

