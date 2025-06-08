package ar.edu.itba.pod.common;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class Address implements DataSerializable, Comparable<Address> {
    private char direction;
    private String name;
    private String type;

    public Address() {
    }

    public Address(char direction, String name, String type) {
        this.direction = direction;
        this.name = name;
        this.type = type;
    }

    public Address(String name) {
        this(' ', name, null);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeChar(direction);
        out.writeUTF(name);
        out.writeUTF(type);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        direction = in.readChar();
        name = in.readUTF();
        type = in.readUTF();
    }

    public char getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, name, type);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Address that &&
                this.direction == that.direction &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public String toString() {
        if (type == null) { // NYC
            return name.replaceFirst("^[0-9]+ ", "");
        } else { // CHI
            return new StringBuilder()
                    .append(direction)
                    .append(' ')
                    .append(name)
                    .append(' ')
                    .append(type)
                    .toString();
        }
    }

    @Override
    public int compareTo(Address o) {
        return String.CASE_INSENSITIVE_ORDER.compare(
                this.toString(), o.toString());
    }
}
