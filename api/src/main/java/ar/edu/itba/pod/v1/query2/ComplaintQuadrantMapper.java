package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.ComplaintQuadrant;
import ar.edu.itba.pod.common.GeoLocation;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantMapper implements Mapper<Long, ComplaintQuadrant, ComplaintQuadrant, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, ComplaintQuadrant row, Context<ComplaintQuadrant, Long> context) {
        context.emit(row, ONE);
    }
}
