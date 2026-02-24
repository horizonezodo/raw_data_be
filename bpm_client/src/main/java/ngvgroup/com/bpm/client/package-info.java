/**
 * BPM Client Library - Package for integrating with Camunda BPM Engine (remote engine).
 * 
 * <p>This package provides a client library for business services to integrate with 
 * Camunda BPM Engine through REST API calls. It is designed to be reusable across 
 * different business services.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Template pattern for process starters, user tasks, and service tasks</li>
 *   <li>Feign client for Camunda REST API communication</li>
 *   <li>File service abstraction (MinIO support)</li>
 *   <li>Document template service (Apache POI support)</li>
 *   <li>Audit validation utilities</li>
 *   <li>Camunda variable utilities</li>
 * </ul>
 * 
 * <h2>Required Dependencies:</h2>
 * <ul>
 *   <li><b>bpm-core-starter</b> (version 1.3.55+) - Required for:
 *     <ul>
 *       <li>BusinessException and ErrorCode</li>
 *       <li>ResponseData wrapper</li>
 *       <li>TenantContext and TenantContextRunner</li>
 *     </ul>
 *   </li>
 *   <li>Spring Boot 3.3.4+</li>
 *   <li>Spring Cloud OpenFeign</li>
 *   <li>Camunda External Task Client 7.20.0+</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <p>Extend the abstract template classes to implement your business logic:</p>
 * <ul>
 *   <li>{@link ngvgroup.com.bpm.client.template.AbstractProcessStarter} - For starting processes</li>
 *   <li>{@link ngvgroup.com.bpm.client.template.AbstractUserTask} - For handling user tasks</li>
 *   <li>{@link ngvgroup.com.bpm.client.template.AbstractServiceTask} - For external service tasks</li>
 * </ul>
 * 
 * <p>See README.md in this package for detailed usage examples.</p>
 * 
 * @since 1.0
 */
package ngvgroup.com.bpm.client;
