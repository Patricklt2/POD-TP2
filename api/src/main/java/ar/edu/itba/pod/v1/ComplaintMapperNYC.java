package ar.edu.itba.pod.v1;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestsNYCRow;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class ComplaintMapperNYC implements Mapper<Long, RequestsNYCRow, Complaint, Long>  {
    private static final Long ONE = 1L;
    @Override
    public void map(Long key, RequestsNYCRow row, Context<Complaint, Long> context) {
        if (row == null) return;

        context.emit(new Complaint(row.getComplaintType(), row.getAgencyName()), ONE);
    }
}
