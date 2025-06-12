package ar.edu.itba.pod.v1.query5;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class ResolutionsNYCMapper implements Mapper<Long, Resolutions, Resolutions, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, Resolutions row, Context<Resolutions, Long> context) {
        if(row.getStatus().equals("Closed"))
            context.emit(row, ONE);
    }
}
