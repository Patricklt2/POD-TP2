package ar.edu.itba.pod.v1.query1;

import ar.edu.itba.pod.common.Complaint;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ComplaintMapper implements Mapper<Long, String, String, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, String row, Context<String, Long> context) {
        context.emit(row, ONE);
    }
}
