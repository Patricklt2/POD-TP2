package ar.edu.itba.pod.v1;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestsCHIRow;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class ComplaintMapperCHI implements Mapper<Long, RequestsCHIRow, Complaint, Long>  {
    private static final Long ONE = 1L;
    @Override
    public void map(Long key, RequestsCHIRow row, Context<Complaint, Long> context) {
        if (row == null) return;

        context.emit(new Complaint(row.getSrShortCode(), row.getOwnerDepartment()), ONE);
    }
}