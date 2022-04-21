package org.helloworld.scribejavawebapp.servlet;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scribe.webapp.oauth.boundary.OAuthService;
import org.scribe.webapp.oauth.boundary.OAuthUser;
import org.scribe.webapp.oauth.boundary.exception.OAuthProviderException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CallbackOAuthProcessServlet
        extends HttpServlet {

    private Log log = LogFactory.getLog(getClass());

    protected void doGetRequest(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {


        OAuthUser client = (OAuthUser) req.getSession().getAttribute("client");

        String oAuthVerifier = req.getParameter("oauth_verifier");
        client.setOAuthVerifier(oAuthVerifier);
        log.info("oAuthVerifier: " + oAuthVerifier);

        String oAuthToken = req.getParameter("oauth_token");

        OAuthService service = new OAuthService();
        try {
            client = service.readingUserData(client);
        } catch (OAuthProviderException e) {
            log.error(e.getMessage(), e);
            throw new ServletException(e);
        }
        log.info("oAuthToken: " + oAuthToken);

        log.info("Calling CallbackOAuthProcessServlet.doGet()");

        log.info("URL: " + fullRequestUrl(req));


        // calling service


        // Logging
        log.info("User: providerUserId => " + client.getProviderUserId());
        log.info("User: nickname => " + client.getNickname());
        log.info("User: name => " + client.getName());
        log.info("User: eMail => " + client.getEMail());

        // put it to session
        req.getSession().setAttribute("user", client);

        res.sendRedirect("remain.html");

    }


    private String fullRequestUrl(HttpServletRequest request) {
        String reqUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null) {
            reqUrl += "?"+queryString;
        }
        return reqUrl;
    }



}
