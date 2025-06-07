package ar.edu.itba.pod.v1.query2;

import ar.edu.itba.pod.common.ComplaintQuadrant;
import ar.edu.itba.pod.common.GeoLocation;
import com.hazelcast.mapreduce.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class ComplaintQuadrantCollator implements Collator<Map.Entry<ComplaintQuadrant, Long>, List<Map.Entry<ComplaintQuadrant, Long>>> {
    @Override
    public List<Map.Entry<ComplaintQuadrant, Long>> collate(Iterable<Map.Entry<ComplaintQuadrant, Long>> values) {
        return StreamSupport.stream(values.spliterator(), false)
                .sorted(Comparator.comparing((Map.Entry<ComplaintQuadrant, Long> entry) -> entry.getKey().getNeighborhood())
                        .thenComparing(entry -> entry.getKey().getLocation().getLatitude())
                        .thenComparing(entry -> entry.getKey().getLocation().getLongitude()))
                .collect(Collectors.toList());
    }
}
