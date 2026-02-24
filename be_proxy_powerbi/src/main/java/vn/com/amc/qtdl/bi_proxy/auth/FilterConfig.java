package vn.com.amc.qtdl.bi_proxy.auth;

import vn.com.amc.qtdl.bi_proxy.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilter) {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter);

        // Áp dụng filter cho tất cả route gateway
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // chạy trước các filter khác

        return registration;
    }
}
