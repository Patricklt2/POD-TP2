package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class NeighborhoodQuadrant implements DataSerializable {
    private GeoLocation location;
    private String neighborhood;

    public NeighborhoodQuadrant() {}

    public NeighborhoodQuadrant(final String neighborhood, final GeoLocation location) {
        this.location = location;
        this.neighborhood = neighborhood;
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
        out.writeDouble(location.getLatitude());
        out.writeDouble(location.getLongitude());
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        neighborhood = in.readUTF();
        location = new GeoLocation(in.readDouble(), in.readDouble());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NeighborhoodQuadrant that &&
                this.neighborhood.equals(that.neighborhood) &&
                this.location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighborhood, location);
    }

    @Override
    public String toString() {
        return "ComplaintQuadrant{" +
                "neighborhood='" + neighborhood + "'" +
                ", location=" + location +
                '}';
    }
}
