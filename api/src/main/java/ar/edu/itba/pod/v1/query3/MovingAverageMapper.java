package ar.edu.itba.pod.v1.query3;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class MovingAverageMapper implements Mapper<Long, Resolutions, Resolutions, Long> {
    private static final Long ONE = 1L;

    private final String city;

    public MovingAverageMapper(String city) {
        this.city = city;
    }

    @Override
    public void map(Long key, Resolutions row, Context<Resolutions, Long> context) {
        boolean isOpen = city.equals("NYC")
                ? !row.getStatus().equals("Closed")
                : row.getStatus().equals("Open");
        if(isOpen) {
            context.emit(row, ONE);
        }
    }
}






