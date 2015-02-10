package urlshortener.uniqueidgenerator.impl;

import org.springframework.stereotype.Service;
import urlshortener.uniqueidgenerator.UniqueIdGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by karthik on 1/11/15.
 */
@Service("uniqueIdGeneratorImpl")
public class UniqueIdGeneratorImpl implements UniqueIdGenerator {

  /**
   * Make-shift unique id. Need distributed atomic integer for scale!
   */
  private final AtomicLong uniqueId = new AtomicLong();

  @Override
  public long getUniqueId() {
    return uniqueId.incrementAndGet();
  }
}
