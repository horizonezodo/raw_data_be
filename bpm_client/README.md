# bpm-client

Thư viện `bpm-client` tách từ service nghiệp vụ, dùng lại cho các microservice tích hợp Camunda (remote engine).

## Nội dung được tách

Toàn bộ package `ngvgroup.com.bpm.client` đã được copy sang:

- `bpm_client/src/main/java/ngvgroup/com/bpm/client`

## Dependency bắt buộc

Thư viện này **bắt buộc** phụ thuộc:

- `com.ngvgroup.bpm:bpm-core-starter`

## Build

```bash
mvn -f bpm_client/pom.xml -DskipTests package
```

