<<<<<<<< HEAD:api/src/main/java/ar/edu/itba/pod/v0Generics/GenericCountMapper.java
package ar.edu.itba.pod.v0Generics;
========
package ar.edu.itba.pod.v0;
>>>>>>>> 3efc8040efcbcbad4e00644bebcf66dbfe588dfc:api/src/main/java/ar/edu/itba/pod/v0/GenericCountMapper.java

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class GenericCountMapper<T> implements Mapper<Long, T, T, Long> {
    private static final Long ONE = 1L;

    @Override
    public void map(Long key, T row, Context<T, Long> context) {
        context.emit(row, ONE);
    }
}
