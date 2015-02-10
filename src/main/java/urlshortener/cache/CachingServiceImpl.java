package urlshortener.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urlshortener.store.GenericDataStore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Make-shift caching service for demo purposes.
 * A true implementation would have a distributed caching service separate from application so
 * that it can be scaled independently (think memcached/redis)
 *
 * Created by karthik on 1/11/15.
 */
@Service("cachingServiceImpl")
public class CachingServiceImpl<K, V> implements CachingService<K, V> {

  private GenericDataStore<K, V> dataStore;

  @Autowired
  public CachingServiceImpl(final GenericDataStore dataStore){
    this.dataStore = dataStore;
  }

  private LoadingCache<K, V> CACHE = CacheBuilder.newBuilder()
                                                 .concurrencyLevel(4) // TODO: Should be tunable from properties file
                                                 .expireAfterWrite(1440, TimeUnit.MINUTES) // TODO: Read from properties
                                                 .build(new CacheLoader<K, V>() {
                                                   @Override
                                                   public V load(K key) throws Exception {
                                                     return dataStore.get(key);
                                                   }
                                                 });


  @Override
  public V get(K key) {
    return CACHE.getIfPresent(key);
  }

  @Override
  public void put(K key, V value) {
    CACHE.put(key, value);
  }

  @Override
  public void invalidate(K key) {
    CACHE.invalidate(key);
  }

}
