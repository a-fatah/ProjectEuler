package com.codility.tasks.solution;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;

import static java.util.Map.Entry.comparingByValue;

interface TimeProvider {
  long getMillis();
}

public class CachingDataStructure {

  private int maxSize;
  private TimeProvider timeProvider;

  private Map<String, String> data = new HashMap<>();
  private Map<String, Long> ttlMap = new HashMap<>();

  CachingDataStructure(TimeProvider timeProvider, int maxSize) {
    this.timeProvider = timeProvider;
    this.maxSize = maxSize;
  }

  public void put(String key, String value, long timeToLeaveInMilliseconds) {
    long ttl = timeProvider.getMillis() + timeToLeaveInMilliseconds;
    if(this.size() == maxSize) {
      if (this.get(key).isPresent()) {
        data.put(key, value);
        ttlMap.put(key, ttl);
      } else {
        // get the key with lowest ttl
        // sort ttlMap in ascending order
        Optional<Map.Entry<String, Long>> lowestTtlEntry =
          ttlMap.entrySet().stream()
          .sorted(comparingByValue())
          .findFirst();

        if (lowestTtlEntry.isPresent()) {
          String lowestTtlKey = lowestTtlEntry.get().getKey();
          data.remove(lowestTtlKey);
          data.put(key, value);
          ttlMap.put(key, ttl);
        }
      }
    }

    // element with ttl lower than lowest ttl should not be added
    OptionalLong lowestTtl = ttlMap.values().stream().mapToLong(v -> v).min();
    if (ttl > lowestTtl.getAsLong()) {
      data.put(key, value);
      ttlMap.put(key, ttl);
    }

    data.put(key, value);
    ttlMap.put(key, ttl);
  }

  public Optional<String> get(String key) {
    // expired elements are considered to be non-existent
    if(data.containsKey(key)) {
      long currentMillis = timeProvider.getMillis();
      // check if time to leave has expired
      if(currentMillis >= ttlMap.get(key)) {
        return Optional.empty();
      }
      return Optional.of(data.get(key));
    }
    return Optional.empty();
  }

  public int size() {
    long currentTime = timeProvider.getMillis();
    return (int) data.keySet().stream()
      .filter(key -> ttlMap.get(key) < currentTime)
      .count();
  }
}
