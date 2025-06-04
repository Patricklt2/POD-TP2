package ar.edu.itba.pod.v1.query1;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestRow;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ComplaintMapper implements Mapper<Long, RequestRow, Complaint, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, RequestRow row, Context<Complaint, Long> context) {
        context.emit(new Complaint(row.getComplaintType(), row.getAgency()), ONE);
    }
}
