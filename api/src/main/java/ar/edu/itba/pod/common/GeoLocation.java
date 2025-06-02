package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

//Might not need to be serializable, will keep it like that for now
public class GeoLocation implements DataSerializable {
    private double latitude;
    private double longitude;

    public GeoLocation() {}

    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeDouble(latitude);
        out.writeDouble(longitude);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude,longitude);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GeoLocation that &&
                Double.compare(this.latitude, that.latitude) == 0 &&
                Double.compare(this.longitude, that.longitude) == 0;
    }
}
