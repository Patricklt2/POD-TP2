package ar.edu.itba.pod.v1.query4;

import ar.edu.itba.pod.common.AddressEvent;
import ar.edu.itba.pod.common.HeuristicNeighbourhood;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class StreetAverageMapper implements Mapper<Long, AddressEvent, String, String> {
    private final HeuristicNeighbourhood neighbourhood;

    public StreetAverageMapper(final HeuristicNeighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @Override
    public void map(final Long key, final AddressEvent row, final Context<String, String> context) {
        if (neighbourhood.hequals(row.getNeighbourhood())) {
            context.emit(row.getAddress().toString(), row.getType());
        }
    }
}
