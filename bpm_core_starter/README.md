# BPM Core Library

Thư viện dùng chung cho các service trong hệ thống BPM.  
Cung cấp các khối hạ tầng cơ bản: auto-configuration, exception, logging, audit, persistence, security, web base…

---

## 1. Cấu trúc package tổng quan

```text
com.ngvgroup.bpm.core
├─ autoconfigure
├─ common
│   ├─ constants
│   ├─ dto
│   ├─ exception
│   └─ util
├─ logging
│   ├─ activity
│   ├─ audit
│   └─ kafka
├─ persistence
│   ├─ config
│   ├─ dto
│   ├─ mapper
│   ├─ model
│   ├─ repository
│   └─ service
├─ security
│   ├─ audit
│   ├─ jwt
│   └─ oauth2
├─ web
│   ├─ controller
│   └─ interceptor
└─ resources
    └─ META-INF/spring/AutoConfiguration.imports

2. Chi tiết từng nhóm
2.1. autoconfigure

    Nhiệm vụ: Khai báo toàn bộ Spring Boot Auto Configuration của lib.
    
    Lớp chính:
    
    BpmCoreAutoConfiguration
    
    BpmCoreSecurityAutoConfiguration
    
    BpmCoreProperties
    
    Thêm mới:
    
    Mọi auto-config mới (ví dụ LoggingAutoConfiguration, AuditAutoConfiguration…) đặt tại đây.
    
    Đừng viết business logic trong package này, chỉ cấu hình wiring bean và property.

2.2. common
    common.constants
    
    Chứa các hằng số dùng chung, ví dụ:
    
    PaginationConstants
    
    Thêm mới: Tất cả hằng số không gắn với 1 feature cụ thể (format datetime, default page size, v.v.).
    
    common.dto
    
    DTO dùng chung cho nhiều module:
    
    ResponseData
    
    OAuth2TokenResponse
    
    Thêm mới:
    
    Wrapper response, paging response, error response… có thể dùng ở bất kỳ service nào.
    
    DTO nghiệp vụ (OrderDTO, UserDTO…) không đặt ở đây.
    
    common.exception
    
    Chuẩn hóa exception & error code:
    
    BusinessException, ErrorCode, IErrorCode
    
    GlobalExceptionHandler (REST exception handler)
    
    Thêm mới:
    
    Exception con kế thừa BusinessException.
    
    Error code mới bổ sung vào ErrorCode.
    
    Logic handle HTTP response cho exception → sửa trong GlobalExceptionHandler.
    
    common.util
    
    Helper tĩnh dùng chung:
    
    DateUtils, FileUtils, SecurityUtils,
    
    StoredProcedureUtils, StringUtils, ValidationUtils
    
    Thêm mới: Utility generic (collection, json…).
    
    Nếu util chỉ phục vụ 1 module (logging/persistence/security), cân nhắc đặt ngay trong module đó thay vì common.util.

2.3. logging
logging.activity

    annotation
    
    @LogActivity: đánh dấu method cần ghi activity log.
    
    @NoActivityLog: bỏ qua log cho method/class.
    
    aspect
    
    ActivityLogAspect: AOP intercept các method có @LogActivity, build ActivityLogMessage.
    
    dto
    
    ActivityLogMessage: payload của activity log.
    
    LoggingToggleResponse: dùng cho API/remote toggle.
    
    toggle
    
    LoggingToggleProperties: cấu hình bật/tắt logging.
    
    LoggingToggleProvider: interface lấy trạng thái toggle.
    
    PropertyBasedLoggingToggleProvider, RemoteLoggingToggleProvider: implement cụ thể.
    
    Thêm mới:
    
    Annotation/log type mới → logging.activity.annotation.
    
    Logic AOP mới hoặc tách nhỏ aspect → logging.activity.aspect.
    
    Nguồn toggle khác (Redis, DB, v.v.) → class mới implement LoggingToggleProvider trong logging.activity.toggle.
    
    logging.audit
    
    annotation
    
    @AuditIgnore: bỏ qua audit một field/entity.
    
    @NoAuditLog: bỏ qua audit cho method/class.
    
    config
    
    AuditAutoConfiguration, HibernateAuditAutoConfiguration: cấu hình cho audit module & Hibernate listener.
    
    domain
    
    EntityChange, ChangeType: mô tả 1 thay đổi trên entity.
    
    dto
    
    AuditFieldChange, AuditLogMessage: DTO dùng khi gửi log audit ra ngoài.
    
    listener
    
    EntityChangeEventListener: nghe event từ Hibernate (insert/update/delete).
    
    service
    
    EntityChangeHandler, EntityChangeService: xử lý, build log, gửi đi (thường qua Kafka).
    
    Thêm mới:
    
    Quy tắc audit/format log → cập nhật trong service & domain.
    
    Listener mới hoặc custom cho use case đặc biệt → listener.
    
    Nếu thêm config cho audit → config.
    
    logging.kafka
    
    config
    
    KafkaProducerConfig: cấu hình Kafka producer dùng cho logging.
    
    service
    
    LoggingKafkaProducerService: service gửi log (activity/audit) lên Kafka.
    
    Thêm mới:
    
    Tất cả thứ liên quan Kafka cho logging (producer/consumer logging) đặt tại đây.
    
    Kafka cho business (order, notification…) nên tách sang module/package khác (messaging.kafka) để không lẫn.

2.4. persistence

    config
    
    StoredProcedureProperties: cấu hình cho stored procedure.
    
    dto
    
    StoredProcedureParameter, StoredProcedureResponse: DTO phục vụ call stored procedure chung.
    
    mapper
    
    BaseMapper: mapper generic cho entity ↔ DTO (nếu dùng).
    
    model
    
    BaseEntity, BaseEntitySimple: base class cho entity (id, audit fields…).
    
    repository
    
    BaseRepository: base JPA repository (generic).
    
    service
    
    BaseService (và các implement/storedprocedure): service tầng persistence dùng chung.
    
    Thêm mới:
    
    Base entity kiểu khác (Auditable, SoftDelete…) → model.
    
    Repository chung mở rộng (specification, paging…) → repository.
    
    Logic stored procedure → service.storedprocedure + dto.
    
    Mapper generic mới → mapper.

    Lưu ý: entity & repository nghiệp vụ (User, Order…) để ở project business, không trong lib.

2.5. security
    security.audit
    
    AuditorConfig, KeycloakAuditorAware
    
    Nhiệm vụ: cung cấp auditor (createdBy/modifiedBy) dựa trên thông tin user (Keycloak).
    
    Thêm mới:
    Nếu hỗ trợ thêm nguồn auth khác (JWT thuần, basic…), thêm AuditorAware/config tương ứng tại đây.
    
    security.jwt
    
    JwtAuthConverter: convert JWT → Authentication.
    
    JwtAuthConverterProperties: cấu hình cho converter.
    
    JwtAuthenticationEntryPoint: xử lý response khi unauthorized/forbidden.
    
    Thêm mới:
    
    Filter JWT, validator, helper… đặt tại security.jwt.
    
    Các cấu hình bổ sung cho JWT → thêm vào đây hoặc autoconfigure.
    
    security.oauth2
    
    feign
    
    OAuth2ClientCredentialsService: lấy token OAuth2 (client credentials) phục vụ call ra ngoài.
    
    Thêm mới:
    
    Interceptor Feign/RestTemplate cho OAuth2 → đặt trong security.oauth2.feign.
    
    Các client/token service khác → trong security.oauth2.

2.6. web
    
    controller
    
    BaseController: base REST controller cho CRUD (dùng chung).
    
    interceptor
    
    RequestIdInterceptor: gắn/đọc header X-Request-ID cho mỗi request.
    
    Thêm mới:
    
    Interceptor chung (HTTP logging, locale, correlation-id…) → web.interceptor.
    
    Base controller/REST helper khác → web.controller.
    
    Nếu có config Spring MVC dùng chung (formatter, converter, CORS…) có thể tạo thêm web.config.

2.7. resources
    
    META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
    
    Danh sách các auto-config để Spring Boot load.
    
    application.yml
    
    File cấu hình mẫu (default) cho lib.

́---------------------------------------------------------------
3. Quy ước khi thêm mới tính năng

    Xác định feature chính:
    
    Liên quan logging? → logging.*
    
    Persistence/jpa/stored procedure? → persistence.*
    
    Security (JWT, OAuth2, Auditor)? → security.*
    
    Tầng web/HTTP? → web.*
    
    Dùng được ở mọi nơi, không phụ thuộc feature? → common.*
    
    Không nhét mọi thứ vào common.util
    
    Utility chỉ dùng cho 1 module nên đặt ngay trong module đó.
    
    Business logic của từng service
    
    Không đưa vào lib core này. Lib chỉ chứa hạ tầng & cross-cutting concern.
    
    Auto-configuration mới
    
    Tạo class trong autoconfigure + đăng ký trong META-INF/spring/...AutoConfiguration.imports.


Sơ đồ tổng thể (Request → Response)

┌────────────┐
│   CLIENT   │
│ (Web / FE) │
└─────┬──────┘
│ HTTP Request
│ Authorization / X-Tenant-Id
▼
┌──────────────────────────────┐
│ Spring Security FilterChain  │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ TenantJwtFilter              │
│------------------------------│
│ - Extract tenant             │
│   • from JWT claim           │
│   • or from header           │
│ - Validate tenant            │
│ - TenantContext.set()        │
│ - OrganizationContext.set()  │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ AuthenticationManager        │
│ Resolver                     │
│------------------------------│
│ TenantIssuerAuthentication   │
│ ManagerResolver              │
│ - Map issuer → tenant        │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ Controller                   │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ Service Layer                │
│ (AuthService / UserService)  │
└──────────┬───────────────────┘
│ JPA / JDBC
▼
┌──────────────────────────────┐
│ RoutingDataSource            │
│------------------------------│
│ - get tenantId from context  │
│ - switch datasource          │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ DataSourceCache              │
│------------------------------│
│ - create or reuse datasource │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ Tenant Database              │
│ (schema / db per tenant)     │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ Response                     │
└──────────┬───────────────────┘
│
▼
┌──────────────────────────────┐
│ finally{}                    │
│ TenantContext.clear()        │
│ OrganizationContext.clear()  │
└──────────────────────────────┘

Sơ đồ MULTI-TENANT SECURITY (JWT / ISSUER)

JWT Token
|
v
TenantJwtFilter
|
+── User Token
|      ├── read tenant claim
|      └── OR map organization
|
+── Service Token
└── read X-Tenant-Id header
|
v
TenantContext.set(tenant)
|
v
TenantIssuerAuthenticationManagerResolver
|
└── issuer → AuthenticationManager

Sơ đồ MULTI-TENANT KAFKA

Producer

Service
|
v
TenantContext
|
v
TenantKafkaProducerInterceptor
|
└── add header X-Tenant-Id
|
v
Kafka Topic

Consumer

Kafka Topic
|
v
TenantKafkaRecordInterceptor
|
├── read X-Tenant-Id
├── TenantContext.set()
|
v
@KafkaListener
|
v
Business Logic
|
v
TenantContext.clear()

Sơ đồ MULTI-TENANT SERVICE → SERVICE (Feign)

Service A
|
| Feign Call
| Authorization + X-Tenant-Id
v
Service B
|
v
TenantJwtFilter
|
v
TenantContext.set()
|
v
RoutingDataSource → Tenant DB