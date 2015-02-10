package urlshortener.service;

/**
 * Created by karthik on 1/11/15.
 */
public interface URLShortenerService {

  /**
   * Returns short URL
   *
   * @param url
   * @return short url
   */
  String shortenUrl(final String url);

  /**
   * Returns the full URL (expanded)
   * @param shortUrl
   *
   * @return full url
   */
  String expandUrl(final String shortUrl);
}
