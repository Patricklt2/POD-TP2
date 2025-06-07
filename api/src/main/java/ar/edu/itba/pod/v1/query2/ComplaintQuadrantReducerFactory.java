package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.ComplaintQuadrant;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantReducerFactory implements ReducerFactory<ComplaintQuadrant, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(ComplaintQuadrant key) {
        return new ComplaintQuadrantReducer();
    }

    private static class ComplaintQuadrantReducer extends Reducer<Long, Long> {
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
