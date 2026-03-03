package com.shorten_url.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnowflakeIdGenerator {

    private final long epoch = 1609459200000L; // Custom epoch (Jan 1, 2021)
    private final long dataCenterIdBits = 5L;
    private final long workerIdBits = 5L;
    private final long sequenceBits = 12L;

    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);
    private final long maxWorkerId = ~(-1L << workerIdBits);
    private final long maxSequence = ~(-1L << sequenceBits);

    private final long dataCenterIdShift = sequenceBits + workerIdBits;
    private final long timestampShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private final long workerIdShift = sequenceBits;

    private final long dataCenterId;
    private final long workerId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(@Value("${snowflake.data-center-id}") long dataCenterId, @Value("${snowflake.worker-id}") long workerId) {
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("Data center ID must be between 0 and %d", maxDataCenterId));
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID must be between 0 and %d", maxWorkerId));
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    public synchronized long generateId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                currentTimestamp = waitUntilNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - epoch) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    private long waitUntilNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }
}
