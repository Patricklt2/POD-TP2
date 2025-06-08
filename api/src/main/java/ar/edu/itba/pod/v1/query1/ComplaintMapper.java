package ar.edu.itba.pod.v1.query1;

import ar.edu.itba.pod.common.Complaint;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ComplaintMapper implements Mapper<Long, Complaint, Complaint, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, Complaint row, Context<Complaint, Long> context) {
        context.emit(row, ONE);
    }
}
