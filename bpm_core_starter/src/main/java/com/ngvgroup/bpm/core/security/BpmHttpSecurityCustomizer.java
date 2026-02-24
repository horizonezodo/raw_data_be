package com.ngvgroup.bpm.core.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Hook point to let other BPM modules (e.g. multitenant) customize {@link HttpSecurity}
 * without owning the {@code SecurityFilterChain} bean.
 */
@FunctionalInterface
public interface BpmHttpSecurityCustomizer {

    void customize(HttpSecurity http) throws Exception;
}
