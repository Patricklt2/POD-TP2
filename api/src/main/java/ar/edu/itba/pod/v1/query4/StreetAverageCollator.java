package ar.edu.itba.pod.v1.query4;

import com.hazelcast.mapreduce.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class StreetAverageCollator implements Collator<Map.Entry<String, Long>, List<Map.Entry<String, Long>>> {
    @Override
    public List<Map.Entry<String, Long>> collate(final Iterable<Map.Entry<String, Long>> values) {
        return StreamSupport.stream(values.spliterator(), false)
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed()
                        .thenComparing(entry -> entry.getKey()))
                .collect(Collectors.toList());
    }
}
