package urlshortener.store;

import com.google.common.base.Optional;

/**
 * Emulate a KV store for persistence.
 *
 * Created by karthik on 1/11/15.
 */
public interface GenericDataStore<K, V> {

  V get(K key);

  V put(K key, V value);

  boolean containsKey(K key);

  // Warning: slow! (This is just an emulation)
  Optional<K> containsValue(V value);

}
