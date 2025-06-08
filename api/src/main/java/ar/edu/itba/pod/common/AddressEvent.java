package ar.edu.itba.pod.common;

import java.io.IOException;
import java.util.Objects;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class AddressEvent implements DataSerializable {
    private Address address;
    private String type;

    public AddressEvent() {}

    public AddressEvent(final Address address, final String type) {
        this.address = address;
        this.type = type;
    }

    public Address getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(address);
        out.writeUTF(type);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        address = in.readObject();
        type = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AddressEvent that &&
                this.type.equals(that.getType()) &&
                this.address.equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, type);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("AddressEvent [type=").append(type)
                .append(", address=").append(address)
                .append("]").toString();
    }
}
