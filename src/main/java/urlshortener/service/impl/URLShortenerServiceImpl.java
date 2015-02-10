package urlshortener.service.impl;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urlshortener.cache.CachingService;
import urlshortener.service.URLShortenerService;
import urlshortener.store.GenericDataStore;
import urlshortener.uniqueidgenerator.UniqueIdGenerator;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Algorithm:
 * -----Generate short url----
 * a) Generate a unique Id;
 * b) Encode this unique Id using base 64 (url) encoding;
 * c) Store the unique id to url mapping in a cache and/or persistence layer.
 *
 * -- Generate full url based on short url---
 *
 * a) Decode short url using base64 encoding;
 * b) Fetch the id and lookup the (distributed) cache.
 *
 * Created by karthik on 1/11/15.
 */
@Service("uRLShortenerServiceImpl")
public class URLShortenerServiceImpl implements URLShortenerService {

  // Persist records. Mapping from unique id to full url.
  private GenericDataStore<Long, String> dataStore;

  // Persist records. Mapping from full url to unique id (should be a secondary index)
  private GenericDataStore<String, Long> reverseLookupStore;

  // Cache records
  private CachingService<Long, String> cachingService;

  // Service to generate unique id
  private UniqueIdGenerator uniqueIdGenerator;

  @Autowired
  public URLShortenerServiceImpl(
      final GenericDataStore genericDataStore,
      final GenericDataStore reverseLookupStore,
      final CachingService cachingService,
      final UniqueIdGenerator uniqueIdGenerator) {
    this.dataStore          = genericDataStore;
    this.reverseLookupStore = reverseLookupStore;
    this.cachingService     = cachingService;
    this.uniqueIdGenerator  = uniqueIdGenerator;
  }

  @Override
  public String shortenUrl(String fullUrl) {

    String shortUrl = null;

    // Need caching service for reverse look ups as well.
    if (reverseLookupStore.containsKey(fullUrl))
    {
      // URL was already shortened, fetch it from datastore.
      long key = reverseLookupStore.get(fullUrl);
      shortUrl = encode(key);
    }
    else
    {
      final long uniqueId   = uniqueIdGenerator.getUniqueId();
      shortUrl = encode(uniqueId);
      dataStore.put(uniqueId, fullUrl);
      reverseLookupStore.put(fullUrl, uniqueId);
      cachingService.put(uniqueId, fullUrl);
    }

    return shortUrl;
  }

  @Override
  public String expandUrl(String shortUrl) {
    return cachingService.get(decode(shortUrl));
  }

  protected String encode(final long num) {
    return BaseEncoding.base64Url().encode(Longs.toByteArray(num));
  }

  protected long decode(final String shortUrl) {
    final byte[] decodedUrl = BaseEncoding.base64Url().decode(shortUrl);
    return ByteBuffer.wrap(decodedUrl).getLong();
  }

}
