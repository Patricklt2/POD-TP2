package ar.edu.itba.pod.v0Generics;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class GenericCountMapper<T> implements Mapper<Long, T, T, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, T row, Context<T, Long> context) {
        context.emit(row, ONE);
    }
}
