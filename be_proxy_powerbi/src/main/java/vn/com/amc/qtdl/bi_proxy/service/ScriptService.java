package vn.com.amc.qtdl.bi_proxy.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ScriptService {

    private static final String SCRIPT = "<script>" +
            "window.addEventListener(\"message\", (event) => {" +
            "if (event.origin === window.location.origin) return;" +
            "document.cookie = \"Token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC\";" +
            "document.cookie = \"Token=\" + event.data + \"; path=/\";" +
            "}, false);" +
            "</script>";

    public void sendScriptResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(SCRIPT);
        response.getWriter().flush();
    }
}
