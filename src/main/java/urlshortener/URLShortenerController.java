package urlshortener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import urlshortener.service.URLShortenerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class URLShortenerController {

  private URLShortenerService service;

  @Autowired
  public URLShortenerController(final URLShortenerService service){
    this.service = service;
  }

  /**
   * Redirect to full URL
   *
   * @param id short url id
   * @param resp
   * @throws Exception
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public void redirectToFullUrl(@PathVariable String id, HttpServletResponse resp) throws Exception {
    String url = null;
    try
    {
       url = service.expandUrl(id);
    }
    catch (Exception e)
    {
      // log this (not in std out though)
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }


    if (url != null)
    {
      resp.sendRedirect(url);
    }
    else
    {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Generate short url
   *
   * @param req
   * @return
   */
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<String> generateShortUrl(HttpServletRequest req) {
    final String url = getUrl(req);
    final String id = service.shortenUrl(url);
    return new ResponseEntity<>("http://go.gl/" + id, HttpStatus.OK);
  }

  protected String getUrl(final HttpServletRequest httpServletRequest) {
    final String queryParams = (httpServletRequest.getQueryString() != null) ? "?" + httpServletRequest.getQueryString() : "";
    final String url = (httpServletRequest.getRequestURI() + queryParams).substring(1);
    return url;
  }

}
