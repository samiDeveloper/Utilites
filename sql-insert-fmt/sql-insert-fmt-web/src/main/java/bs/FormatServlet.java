package bs;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/format")
public class FormatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletConfig servletConfig;

    @Override
    public void init(javax.servlet.ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Config cfg = createConfig(request);
        String sql = fetchSql(request);

        setRequestAtts(request, cfg, sql);

        InsertFormatterResponse formatRs = InsertFormatter.format(sql, cfg);

        final String formatted;
        if (formatRs.isOk()) {
            formatted = formatRs.getFormattedSql();
        } else {
            formatted = formatRs.getError();
        }

        request.setAttribute("formatted", formatted);
        servletConfig.getServletContext().getRequestDispatcher("/format.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Config cfg = createConfig(request);
        String sql = fetchSql(request);

        setRequestAtts(request, cfg, sql);

        servletConfig.getServletContext().getRequestDispatcher("/format.jsp").forward(request, response);
    }

    private static void setRequestAtts(HttpServletRequest request, Config cfg, String sql) {
        request.setAttribute("sql", sql);
        request.setAttribute("indent", cfg.getIndent());
        request.setAttribute("spacing", cfg.getSpacingBetweenValues());
        request.setAttribute("width", cfg.getLineWidth());

        String permlink = createSettingsPermlink(request, cfg);
        request.setAttribute("permlink", permlink);
    }

    private static String createSettingsPermlink(HttpServletRequest request, Config cfg) {
        String link = request.getRequestURL().append("?indent=").append(cfg.getIndent()).append("&spacing=")
                .append(cfg.getSpacingBetweenValues()).append("&width=").append(cfg.getLineWidth()).toString();
        return link;
    }

    private static String fetchSql(HttpServletRequest request) {
        String sql = request.getParameter("sql");

        if (sql == null) {
            InputStream is = FormatServlet.class.getResourceAsStream("/sql-default.sql");
            sql = convertStreamToString(is);
        }

        return sql;
    }

    private static String convertStreamToString(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

    private static Config createConfig(HttpServletRequest request) {
        Config cfg = new Config();
        try {
            cfg.setIndent(Integer.valueOf(request.getParameter("indent")));
        } catch (NumberFormatException nfe) {
        }
        try {
            cfg.setSpacingBetweenValues(Integer.valueOf(request.getParameter("spacing")));
        } catch (NumberFormatException nfe) {
        }
        try {
            cfg.setLineWidth(Integer.valueOf(request.getParameter("width")));
        } catch (NumberFormatException nfe) {
        }
        return cfg;
    }
}
