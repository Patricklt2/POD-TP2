package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.ComplaintQuadrant;
import ar.edu.itba.pod.common.NeighborhoodQuadrant;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantMapper implements Mapper<Long, ComplaintQuadrant, NeighborhoodQuadrant, String> {
    @Override
    public void map(Long key, ComplaintQuadrant row, Context<NeighborhoodQuadrant, String> context) {
        context.emit(new NeighborhoodQuadrant(row.getNeighborhood(), row.getLocation()), row.getType());
    }
}
