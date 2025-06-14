package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.NeighborhoodQuadrant;

import com.hazelcast.mapreduce.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantCollator
        implements Collator<Map.Entry<NeighborhoodQuadrant, String>, List<Map.Entry<NeighborhoodQuadrant, String>>> {
    @Override
    public List<Map.Entry<NeighborhoodQuadrant, String>> collate(
            final Iterable<Map.Entry<NeighborhoodQuadrant, String>> values) {
        return StreamSupport.stream(values.spliterator(), false)
                .sorted(Comparator
                        .comparing((Map.Entry<NeighborhoodQuadrant, String> entry) -> entry.getKey().getNeighborhood())
                        .thenComparing(entry -> entry.getKey().getLocation().getLatitude())
                        .thenComparing(entry -> entry.getKey().getLocation().getLongitude()))
                .collect(Collectors.toList());
    }
}
