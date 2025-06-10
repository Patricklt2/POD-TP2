package ar.edu.itba.pod.v1.query3;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class MovingAverageCollator implements Collator<Map.Entry<Resolutions, Long>, List<String>> {
    @Override
    public List<String> collate(Iterable<Map.Entry<Resolutions, Long>> values) {
        int w = Integer.parseInt(System.getProperty("w"));

        Map<String, TreeMap<Integer, long[]>> grouped = new TreeMap<>();

        for (Map.Entry<Resolutions, Long> entry : values) {
            Resolutions r = entry.getKey();

            TreeMap<Integer, long[]> yearsMap = grouped
                    .computeIfAbsent(r.getAgency(), k -> new TreeMap<>());

            long[] months = yearsMap.computeIfAbsent(r.getYear(), k -> new long[12]);
            months[r.getMonth() - 1] = entry.getValue();
        }

        List<String> result = new ArrayList<>();

        for (Map.Entry<String, TreeMap<Integer, long[]>> agencyEntry : grouped.entrySet()) {
            String agency = agencyEntry.getKey();

            for (Map.Entry<Integer, long[]> yearEntry : agencyEntry.getValue().entrySet()) {
                int year = yearEntry.getKey();
                long[] monthly = yearEntry.getValue();

                long rollingSum = 0;

                for (int i = 0; i < 12; i++) {
                    rollingSum += monthly[i];

                    if (i + 1 >= w) {
                        if (i + 1 > w) {
                            rollingSum -= monthly[i - w];
                        }
                        double avg = rollingSum / (double) w;
                        result.add(String.format("%s;%d;%d;%.2f", agency, year, i + 1, Math.floor(avg * 100) / 100));
                    } else {
                        double avg = rollingSum / (double) (i + 1);
                        result.add(String.format("%s;%d;%d;%.2f", agency, year, i + 1, Math.floor(avg * 100) / 100));
                    }
                }
            }
        }

        return result;
    }
}




