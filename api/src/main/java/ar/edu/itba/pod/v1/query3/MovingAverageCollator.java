package ar.edu.itba.pod.v1.query3;

import ar.edu.itba.pod.common.Resolutions;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class MovingAverageCollator implements Collator<Map.Entry<Resolutions, Long>, List<String>> {
    private final int w;

    public MovingAverageCollator(int w) {
        this.w = w;
    }

    @Override
    public List<String> collate(Iterable<Map.Entry<Resolutions, Long>> values) {
        final Map<String, TreeMap<Integer, long[]>> grouped = new TreeMap<>();

        for (final Map.Entry<Resolutions, Long> entry : values) {
            final Resolutions r = entry.getKey();

            final TreeMap<Integer, long[]> yearsMap = grouped
                    .computeIfAbsent(r.getAgency(), k -> new TreeMap<>());

            final long[] months = yearsMap.computeIfAbsent(r.getYear(), k -> new long[12]);
            months[r.getMonth() - 1] = entry.getValue();
        }

        final List<String> result = new ArrayList<>();

        for (final Map.Entry<String, TreeMap<Integer, long[]>> agencyEntry : grouped.entrySet()) {
            final String agency = agencyEntry.getKey();

            for (Map.Entry<Integer, long[]> yearEntry : agencyEntry.getValue().entrySet()) {
                final int year = yearEntry.getKey();
                final long[] monthly = yearEntry.getValue();

                long rollingSum = 0;
                for (int i = 0; i < 12; i++) {
                    rollingSum += monthly[i];
                    double avg;

                    if (i + 1 >= w) {
                        if (i + 1 > w) {
                            rollingSum -= monthly[i - w];
                        }

                        avg = rollingSum / (double) w;
                    } else {
                        avg = rollingSum / (double) (i + 1);
                    }

                    final double floor = Math.floor(avg * 100) / 100.0;
                    final StringBuilder sb = new StringBuilder()
                            .append(agency).append(';')
                            .append(year).append(';')
                            .append(i + 1).append(';')
                            .append(String.format(Locale.ROOT, "%.2f", floor));

                    result.add(sb.toString());
                }
            }
        }

        return result;
    }
}
