package urlshortener.uniqueidgenerator;

/**
 * Created by karthik on 1/11/15.
 */
public interface UniqueIdGenerator {
  /**
   * A simple unique Id generation service that should scale across multiple machines.
   * @return
   */
  long getUniqueId();
}
