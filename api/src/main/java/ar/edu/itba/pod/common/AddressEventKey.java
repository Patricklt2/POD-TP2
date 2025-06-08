package ar.edu.itba.pod.common;

import java.io.IOException;
import java.util.Objects;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

public class AddressEventKey implements DataSerializable {
    private long id;
    private String neighbourhood;

    public AddressEventKey() {}

    public AddressEventKey(final long id, final String neighbourhood) {
        this.id = id;
        this.neighbourhood = neighbourhood;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(id);
        out.writeUTF(neighbourhood);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = in.readLong();
        neighbourhood = in.readUTF();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof AddressEventKey that &&
                this.id == that.id &&
                this.neighbourhood.equals(that.getNeighbourhood());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, neighbourhood);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("AddressEvent [id=").append(id)
                .append(", neighbourhood=").append(neighbourhood)
                .append("]").toString();
    }
}
