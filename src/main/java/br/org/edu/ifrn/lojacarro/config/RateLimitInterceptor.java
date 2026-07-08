package br.org.edu.ifrn.lojacarro.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // Capacidade máxima de requisições parametrizada
    @Value("${app.rate-limit.capacity:1000}")
    private int capacity;

    // Tempo de recarga do balde parametrizado
    @Value("${app.rate-limit.duration-minutes:1}")
    private int durationMinutes;

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofMinutes(durationMinutes))))
                .build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Limite de chamadas excedido. Aguarde antes de tentar novamente.\"}");
        return false;
    }
}