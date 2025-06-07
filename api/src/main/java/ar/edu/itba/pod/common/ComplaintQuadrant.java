package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class ComplaintQuadrant implements DataSerializable, Comparable<ComplaintQuadrant> {
    private String type;
    private GeoLocation location;
    private String neighborhood;

    public ComplaintQuadrant() {}

    public ComplaintQuadrant(final String type, final String neighborhood, final Double latitude, final Double longitude) {
        this.type = type;
        this.location = new GeoLocation(latitude, longitude);
        this.neighborhood = neighborhood;
    }

    public String getType() {
        return type;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(neighborhood);
        out.writeUTF(type);
        out.writeDouble(location.getLatitude());
        out.writeDouble(location.getLongitude());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        neighborhood = in.readUTF();
        type = in.readUTF();
        location = new GeoLocation(in.readDouble(), in.readDouble());
    }

    @Override
    public int compareTo(ComplaintQuadrant other) {
        int cmp = this.neighborhood.compareTo(other.neighborhood);
        if (cmp != 0) return cmp;
        return this.location.compareTo(other.location);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ComplaintQuadrant that &&
                this.type.equals(that.type) &&
                this.location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, location);
    }

    @Override
    public String toString() {
        return "ComplaintQuadrant{" +
                "type='" + type + '\'' +
                ", location=" + location +
                '}';
    }
}
