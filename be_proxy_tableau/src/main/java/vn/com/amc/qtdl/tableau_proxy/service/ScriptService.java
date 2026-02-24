package vn.com.amc.qtdl.tableau_proxy.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
public class ScriptService {

    private static final String SCRIPT = "<script>window.addEventListener(\"message\", (event) => {if (event.origin === window.location.origin) {return;} document.cookie = \"Token=; path=/; Secure; SameSite=None; expires=Thu, 01 Jan 1970 00:00:00 UTC\"; document.cookie = \"Token=\" + event.data + \"; path=/; Secure; SameSite=None\";}, false);</script>";


    public Mono<Void> sendScriptResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.TEXT_HTML);

        DataBuffer buffer = response.bufferFactory().wrap(SCRIPT.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
