package ar.edu.itba.pod.v1.query5;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("deprecation")
public class ResolutionsCollator implements Collator<Map.Entry<Resolutions, Long>, List<Map.Entry<Resolutions, Long>>> {
    @Override
    public List<Map.Entry<Resolutions, Long>> collate(Iterable<Map.Entry<Resolutions, Long>> values) {
        List<Map.Entry<Resolutions, Long>> list = StreamSupport.stream(values.spliterator(), false)
                .sorted(Comparator.<Map.Entry<Resolutions, Long>, String>comparing(e -> e.getKey().getAgency()).reversed()
                        .thenComparingInt(entry -> entry.getKey().getYear())
                        .thenComparingInt(entry -> entry.getKey().getMonth()))
                .collect(Collectors.toList());

        long aux = 0;
        String agency = "";
        int year = 0;


        for (Map.Entry<Resolutions, Long> entry : list) {
            if (entry.getKey().getYear() != year || !entry.getKey().getAgency().equals(agency)) {
                aux = 0;
                agency = entry.getKey().getAgency();
                year = entry.getKey().getYear();
            }

            aux += entry.getValue();
            entry.setValue(aux);
        }

        return list;
    }
}
