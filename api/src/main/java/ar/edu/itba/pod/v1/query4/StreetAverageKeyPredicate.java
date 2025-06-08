package ar.edu.itba.pod.v1.query4;

import com.hazelcast.mapreduce.KeyPredicate;

import ar.edu.itba.pod.common.AddressEventKey;

@SuppressWarnings("deprecation")
public class StreetAverageKeyPredicate implements KeyPredicate<AddressEventKey> {
    private final String neighbourhood;

    public StreetAverageKeyPredicate(final String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    @Override
    public boolean evaluate(AddressEventKey key) {
        return key.getNeighbourhood().equals(neighbourhood);
    }
}
