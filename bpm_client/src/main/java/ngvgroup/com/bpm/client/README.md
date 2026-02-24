# BPM Client Library

Thư viện client để tích hợp với Camunda BPM Engine (remote engine) cho các microservice nghiệp vụ.

## Mục đích

Package `ngvgroup.com.bpm.client` được thiết kế để có thể tách thành thư viện độc lập, cho phép các service nghiệp vụ khác tái sử dụng logic tích hợp với Camunda BPM.

## Dependencies bắt buộc

### 1. bpm-core-starter

**Version:** `1.3.55` (hoặc tương thích)

**Các class được sử dụng:**
- `com.ngvgroup.bpm.core.common.exception.BusinessException` - Exception handling
- `com.ngvgroup.bpm.core.common.exception.ErrorCode` - Error code definitions
- `com.ngvgroup.bpm.core.common.dto.ResponseData<T>` - Response wrapper cho Feign client
- `com.ngvgroup.bpm.core.persistence.config.multitenant.TenantContext` - Multi-tenant context
- `com.ngvgroup.bpm.core.security.tenant.TenantContextRunner` - Tenant context runner

**Lý do:** Đây là dependency bắt buộc vì package này được thiết kế để hoạt động trong hệ sinh thái NGV BPM, sử dụng các utilities và patterns chung từ `bpm-core-starter`.

### 2. Spring Boot Dependencies

- `spring-boot-starter-web` - Web framework
- `spring-cloud-openfeign` - Feign client cho REST API calls
- `spring-boot-starter-oauth2-client` - OAuth2 client cho authentication
- `camunda-bpm-spring-boot-starter-external-task-client` - Camunda external task client

### 3. Optional Dependencies

- `io.minio:minio` - MinIO client (nếu sử dụng MinIO cho file storage)
- `org.apache.poi:poi-ooxml` - Apache POI (nếu sử dụng document template service)

## Cấu trúc Package

```
ngvgroup.com.bpm.client/
├── annotation/          # Annotations (AuditField)
├── config/              # Configuration classes
├── constant/            # Constants
├── dto/                 # Data Transfer Objects
│   ├── camunda/        # Camunda-specific DTOs
│   ├── request/        # Request DTOs
│   ├── response/       # Response DTOs
│   ├── shared/         # Shared DTOs
│   └── variable/       # Variable DTOs
├── exception/          # Exception classes và error codes
├── feign/              # Feign client interfaces
├── service/            # Service interfaces và implementations
├── template/           # Abstract template classes
└── utils/              # Utility classes
```

## Cách sử dụng

### 1. Thêm dependency vào pom.xml

```xml
<dependency>
    <groupId>com.ngvgroup.bpm</groupId>
    <artifactId>bpm-core-starter</artifactId>
    <version>1.3.55</version>
</dependency>
```

### 2. Cấu hình Spring Boot

Không cần `@ComponentScan` / `@EnableFeignClients` thủ công nữa (thư viện đã có Spring Boot auto-configuration):

```java
// Không cần cấu hình scan cho bpm-client
```

### 3. Cấu hình application.yml

```yaml
service:
  bpm:
    url: http://bpm-engine-service:8080

bpm:
  core:
    oauth2:
      token-uri: http://auth-server/oauth/token
      client-id: your-client-id
      client-secret: your-client-secret

# Optional: MinIO configuration
bpm:
  client:
    minio:
      enabled: true
minio:
  url: http://minio:9000
  access-key: your-access-key
  secret-key: your-secret-key
  bucket: your-bucket-name
```

### 4. Sử dụng Template Classes

#### AbstractProcessStarter

```java
@Service
@RequiredArgsConstructor
public class YourProcessStarter extends AbstractProcessStarter<YourBusinessDto> {
    
    private final BpmFeignClient bpmFeignClient;
    private final FileService fileService;
    
    @Override
    protected void validateSpecificLogic(YourBusinessDto businessData) {
        // Your validation logic
    }
    
    @Override
    protected String saveBusinessData(String processDefinitionKey, YourBusinessDto businessData) {
        // Save business data and return process instance code
        return "PROCESS-001";
    }
    
    @Override
    protected String attachBusinessFile(YourBusinessDto businessData, 
                                       MultiValueMap<String, MultipartFile> multiFileMap) {
        // Attach business files and return process file code
        return "FILE-001";
    }
    
    @Override
    protected ProcessData prepareProcessData(StartRequest<YourBusinessDto> dto) {
        // Prepare process-specific data
        return new ProcessData();
    }
}
```

#### AbstractUserTask

```java
@Service
@RequiredArgsConstructor
public class YourUserTask extends AbstractUserTask<YourBusinessDto> {
    
    private final BpmFeignClient bpmFeignClient;
    private final FileService fileService;
    
    @Override
    protected YourBusinessDto getBusinessData(TaskViewBpmData bpmData) {
        // Load business data from database
        return yourService.findById(bpmData.getBusinessKey());
    }
    
    @Override
    protected void validateSpecificLogic(YourBusinessDto businessData) {
        // Your validation logic
    }
    
    @Override
    protected String getProcessInstanceCode(YourBusinessDto businessData) {
        return businessData.getProcessInstanceCode();
    }
    
    @Override
    protected String attachBusinessFile(YourBusinessDto businessData, 
                                       MultiValueMap<String, MultipartFile> multiFileMap) {
        // Attach business files
        return "FILE-001";
    }
    
    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<YourBusinessDto> dto) {
        // Prepare mail variables
        return new MailVariableDto();
    }
    
    @Override
    protected void saveBusinessData(YourBusinessDto businessData) {
        // Save business data
        yourService.save(businessData);
    }
}
```

#### AbstractServiceTask

```java
@Component
public class YourServiceTask extends AbstractServiceTask {
    
    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);
        TaskBpmData taskBpmData = CamundaVariablesUtil.getTaskBpmData(externalTask);
        
        // Your business logic here
        
        // Set variables if needed
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", "success");
        externalTaskService.complete(externalTask, variables);
    }
}
```

## Error Handling

Package sử dụng `BusinessException` từ `bpm-core-starter` và định nghĩa các error codes riêng trong `BpmClientErrorCode`:

- `INVALID_JSON_FORMAT` (4001) - Lỗi định dạng JSON
- `INVALID_AUDIT_DATA` (4002) - Lỗi dữ liệu audit không hợp lệ

## Best Practices

1. **Luôn extend các abstract classes** thay vì implement trực tiếp interfaces
2. **Sử dụng BpmClientErrorCode** cho các error codes mới trong package
3. **Validate business logic** trong các method `validateSpecificLogic()`
4. **Handle exceptions** đúng cách - BusinessException sẽ được xử lý bởi global exception handler từ bpm-core-starter

## Version Compatibility

- Java: 17+
- Spring Boot: 3.3.4+
- Camunda: 7.20.0+

## License

Internal NGV Group library.
