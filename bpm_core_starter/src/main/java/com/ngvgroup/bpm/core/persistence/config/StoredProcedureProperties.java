package com.ngvgroup.bpm.core.persistence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties cấu hình cho stored procedure
 */
@Data
@ConfigurationProperties(prefix = "bpm.core.stored-procedure")
public class StoredProcedureProperties {
    
    /**
     * Bật/tắt chức năng stored procedure
     */
    private boolean enabled = true;
    
    /**
     * Cấu hình connection pool
     */
    private ConnectionPool connectionPool = new ConnectionPool();
    
    /**
     * Cấu hình timeout
     */
    private Timeout timeout = new Timeout();
    
    /**
     * Cấu hình retry
     */
    private Retry retry = new Retry();
    
    /**
     * Cấu hình logging
     */
    private Logging logging = new Logging();
    
    @Data
    public static class ConnectionPool {
        /**
         * Kích thước ban đầu của pool
         */
        private int initialSize = 5;
        
        /**
         * Kích thước tối đa của pool
         */
        private int maxSize = 20;
        
        /**
         * Số connection tối thiểu trong pool
         */
        private int minIdle = 5;
        
        /**
         * Thời gian chờ lấy connection (milliseconds)
         */
        private long connectionTimeout = 30000;
        
        /**
         * Thời gian tối đa connection có thể idle (milliseconds)
         */
        private long idleTimeout = 600000;
        
        /**
         * Thời gian tối đa connection có thể tồn tại (milliseconds)
         */
        private long maxLifetime = 1800000;
    }
    
    @Data
    public static class Timeout {
        /**
         * Timeout cho connection (milliseconds)
         */
        private long connection = 30000;
        
        /**
         * Timeout cho query (milliseconds)
         */
        private long query = 60000;
    }
    
    @Data
    public static class Retry {
        /**
         * Số lần thử lại tối đa
         */
        private int maxAttempts = 3;
        
        /**
         * Thời gian chờ giữa các lần thử lại (milliseconds)
         */
        private long backoffDelay = 1000;
        
        /**
         * Bật/tắt retry
         */
        private boolean enabled = true;
    }
    
    @Data
    public static class Logging {
        /**
         * Bật/tắt logging
         */
        private boolean enabled = true;
        
        /**
         * Level logging
         */
        private String level = "INFO";
        
        /**
         * Bật/tắt log parameters
         */
        private boolean logParameters = false;
        
        /**
         * Bật/tắt log execution time
         */
        private boolean logExecutionTime = true;
    }
} 