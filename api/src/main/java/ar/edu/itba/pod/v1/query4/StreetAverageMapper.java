package ar.edu.itba.pod.v1.query4;

import ar.edu.itba.pod.common.AddressEvent;
import ar.edu.itba.pod.common.AddressEventKey;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class StreetAverageMapper implements Mapper<AddressEventKey, AddressEvent, String, String> {
    @Override
    public void map(AddressEventKey key, AddressEvent row, Context<String, String> context) {
        context.emit(row.getAddress().toString(), row.getType());
    }
}
