package ar.edu.itba.pod.v1.query1;

import ar.edu.itba.pod.common.Complaint;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ComplaintReducerFactory implements ReducerFactory<String, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(String key) {
        return new ComplaintReducer();
    }

    private static class ComplaintReducer extends Reducer<Long, Long> {
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
