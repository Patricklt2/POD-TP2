package ar.edu.itba.pod.v1;

import ar.edu.itba.pod.common.Complaint;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class ComplaintReducerFactory implements ReducerFactory<Complaint, Long, Long> {
    @Override
    public Reducer<Long, Long> newReducer(Complaint key) {
        return new ComplaintReducer();
    }

    private class ComplaintReducer extends Reducer<Long, Long> {
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
