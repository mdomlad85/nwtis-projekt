/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.filteri;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.mdomladov.ejb.eb.Dnevnik;
import org.foi.nwtis.mdomladov.ejb.sb.facades.DnevnikFacade;
import org.foi.nwtis.mdomladov.podaci.Korisnik;

/**
 *
 * @author Zeus
 */
@WebFilter(filterName = "KontrolaPristupa", urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.ASYNC, DispatcherType.REQUEST})
public class KontrolaPristupa implements Filter {

    @EJB
    private DnevnikFacade db;

    public static final String KORISNIK_ATTRIBUTE = "korisnik";

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public KontrolaPristupa() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("KontrolaPristupa:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("KontrolaPristupa:DoAfterProcessing");
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        long startTime = System.currentTimeMillis();
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        HttpSession session = httpReq.getSession();

        if (httpReq.getRequestURL().toString().endsWith(".xhtml")
                || httpReq.getRequestURL().toString().endsWith("/")) {

            httpReq.setCharacterEncoding("UTF-8");
            httpResp.setCharacterEncoding("UTF-8");
            httpResp.setContentType("application/json");

            Korisnik korisnik = (Korisnik) session.getAttribute(KORISNIK_ATTRIBUTE);
            Dnevnik dnevnik = new Dnevnik();
            dnevnik.setUrl(httpReq.getRequestURL().toString());
            dnevnik.setIpadresa(httpReq.getRemoteAddr());
            dnevnik.setVrijeme(new Date());
            dnevnik.setStatus(200);

            if (korisnik == null) {
                dnevnik.setKorisnik("anonimno");
                if (dnevnik.getUrl().toLowerCase().contains("privatno")) {
                    dnevnik.setStatus(403);
                    httpResp.sendRedirect("/mdomladov_aplikacija_2_2/faces/login.xhtml");
                } else {
                    chain.doFilter(request, response);
                }
            } else {
                dnevnik.setKorisnik(korisnik.getKorisnickoIme());
                chain.doFilter(request, response);
            }
            dnevnik.setTrajanje((int) (System.currentTimeMillis() - startTime));
            try{
                db.create(dnevnik);
            } catch (Exception ex){
                 log("KontrolaPristupa:" + ex.getMessage());
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("KontrolaPristupa:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("KontrolaPristupa()");
        }
        StringBuffer sb = new StringBuffer("KontrolaPristupa(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
