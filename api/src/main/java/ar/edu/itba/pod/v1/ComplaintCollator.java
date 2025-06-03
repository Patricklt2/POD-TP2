package ar.edu.itba.pod.v1;

import ar.edu.itba.pod.common.Complaint;

import com.hazelcast.mapreduce.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class ComplaintCollator implements Collator<Map.Entry<Complaint, Long>, List<Map.Entry<Complaint, Long>>> {
    @Override
    public List<Map.Entry<Complaint, Long>> collate(Iterable<Map.Entry<Complaint, Long>> values) {
        return StreamSupport.stream(values.spliterator(), false)
                .sorted(Comparator.<Map.Entry<Complaint, Long>>comparingLong(Map.Entry::getValue).reversed()
                        .thenComparing(entry -> entry.getKey().getType())
                        .thenComparing(entry -> entry.getKey().getAgency()))
                .collect(Collectors.toList());
    }
}
