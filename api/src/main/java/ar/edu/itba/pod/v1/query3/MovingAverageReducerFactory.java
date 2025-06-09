package ar.edu.itba.pod.v1.query3;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovingAverageReducerFactory implements ReducerFactory<Resolutions, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(Resolutions key) {
        return new MovingAverageReducer();
    }

    private static class MovingAverageReducer extends Reducer<Long, Long> {
        private long count = 0;

        @Override
        public void reduce(Long value) {
            count += value;
        }

        @Override
        public Long finalizeReduce() {
            return count;
        }
    }
}




