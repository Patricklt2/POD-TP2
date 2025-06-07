package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.Complaint;
import ar.edu.itba.pod.common.RequestRow;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class GeoLocationMapper implements Mapper<Long, Complaint, Complaint, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, Complaint row, Context<Complaint, Long> context) {
        context.emit(row, ONE);
    }
}
