package urlshortener.cache;

/**
 * Created by karthik on 1/11/15.
 */
public interface CachingService<K, V> {
  V get(K key);
  void put(K key, V value);
  void invalidate(K key);
}
