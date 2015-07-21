package eu.dzhw.fdz.metadatamanagement.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Simple Controller which returns a welcome page.
 * @author René Reitmann
 */
@Controller
public class WelcomeController {
    
    /**
     * Show welcome page
     * @return welcome.html
     */
    @RequestMapping("/")
    public DeferredResult<String> showWelcomeFile() {
	DeferredResult<String> result = new DeferredResult<String>();
	result.setResult("welcome");
	return result;
    }
}
