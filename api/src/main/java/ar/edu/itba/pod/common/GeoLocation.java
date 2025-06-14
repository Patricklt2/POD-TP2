package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class GeoLocation implements DataSerializable, Comparable<GeoLocation> {
    private long latitude;
    private long longitude;

    public GeoLocation() {
    }

    public GeoLocation(long latitude, long longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(latitude);
        out.writeLong(longitude);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        latitude = in.readLong();
        longitude = in.readLong();
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    @Override
    public int compareTo(GeoLocation o) {
        int cmp = Long.compare(this.latitude, o.latitude);
        if (cmp != 0)
            return cmp;
        return Long.compare(this.longitude, o.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GeoLocation that &&
                this.latitude == that.latitude &&
                this.longitude == that.longitude;
    }
}
