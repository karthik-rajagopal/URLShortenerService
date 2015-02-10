package urlshortener.store.impl;

import com.google.common.base.Optional;
import org.springframework.stereotype.Service;
import urlshortener.store.GenericDataStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Poor man's persistence layer.
 *
 * Created by karthik on 1/11/15.
 */
@Service("genericDataStoreImpl")
public class GenericDataStoreImpl<K, V> implements GenericDataStore<K, V> {

  private final ConcurrentMap<K, V> store;

  private GenericDataStoreImpl() {
    store = new ConcurrentHashMap<>();
  }

  @Override
  public V get(K key) {
    return store.get(key);
  }

  @Override
  public V put(K key, V value) {
    return store.putIfAbsent(key, value);
  }

  @Override
  public boolean containsKey(K key) {
    return store.containsKey(key);
  }

  /**
   * This is slow. An actual implementation would have a secondary index on V.
   *
   * @param value
   * @return
   */
  @Override
  public Optional<K> containsValue(V value) {
    for (Map.Entry<K, V> entry : store.entrySet()){
      if(entry.getValue().equals(value)){
        return Optional.of(entry.getKey());
      }
    }
    return Optional.absent();
  }
}
