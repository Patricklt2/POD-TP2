package ar.edu.itba.pod.common;

import java.io.IOException;
import java.util.Objects;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class AddressEvent implements DataSerializable {
    private String neighbourhood;
    private Address address;
    private String type;

    public AddressEvent() {}

    public AddressEvent(final String neighbourhood, final Address address, final String type) {
        this.neighbourhood = neighbourhood;
        this.address = address;
        this.type = type;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public Address getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(neighbourhood);
        out.writeObject(address);
        out.writeUTF(type);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        neighbourhood = in.readUTF();
        address = in.readObject();
        type = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AddressEvent that &&
                this.neighbourhood.equals(that.getNeighbourhood()) &&
                this.type.equals(that.getType()) &&
                this.address.equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighbourhood, address, type);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("AddressEvent [type=").append(type)
                .append(", address=").append(address)
                .append(", neighbourhood=").append(neighbourhood)
                .append("]").toString();
    }
}
