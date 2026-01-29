package com.wbw.dubbo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * Dubbo配置属性类
 */
@ConfigurationProperties(prefix = "dubbo")
public class DubboProperties {

    /**
     * 应用配置
     */
    private Application application = new Application();

    /**
     * 注册中心配置
     */
    private Registry registry = new Registry();

    /**
     * 协议配置
     */
    private Protocol protocol = new Protocol();

    /**
     * 服务提供者配置
     */
    private Provider provider = new Provider();

    /**
     * 服务消费者配置
     */
    private Consumer consumer = new Consumer();

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * 应用配置
     */
    public static class Application {
        /**
         * 应用名称
         */
        private String name;

        /**
         * 模块名称
         */
        private String module;

        /**
         * 版本
         */
        private String version;

        /**
         * 组织
         */
        private String organization;

        /**
         * 环境
         */
        private String environment;

        /**
         * 负责人
         */
        private String owner;

        /**
         * 应用参数
         */
        private Map<String, String> parameters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getEnvironment() {
            return environment;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }

    /**
     * 注册中心配置
     */
    public static class Registry {
        /**
         * 注册中心地址
         */
        private String address;

        /**
         * 注册中心用户名
         */
        private String username;

        /**
         * 注册中心密码
         */
        private String password;

        /**
         * 注册中心超时时间
         */
        private Duration timeout = Duration.ofSeconds(30);

        /**
         * 注册中心集群
         */
        private String cluster;

        /**
         * 注册中心分组
         */
        private String group;

        /**
         * 注册中心命名空间
         */
        private String namespace;

        /**
         * 注册中心参数
         */
        private Map<String, String> parameters;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Duration getTimeout() {
            return timeout;
        }

        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }

        public String getCluster() {
            return cluster;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }

    /**
     * 协议配置
     */
    public static class Protocol {
        /**
         * 协议名称
         */
        private String name = "dubbo";

        /**
         * 协议端口
         */
        private Integer port = 20880;

        /**
         * 协议主机
         */
        private String host;

        /**
         * 线程池类型
         */
        private String threadpool = "fixed";

        /**
         * 线程池大小
         */
        private Integer threads = 200;

        /**
         * 线程池队列大小
         */
        private Integer queues = 0;

        /**
         * 序列化方式
         */
        private String serialization = "hessian2";

        /**
         * 超时时间
         */
        private Duration timeout = Duration.ofSeconds(10);

        /**
         * 连接数
         */
        private Integer connections = 10;

        /**
         * 权重
         */
        private Integer weight = 100;

        /**
         * 协议参数
         */
        private Map<String, String> parameters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getThreadpool() {
            return threadpool;
        }

        public void setThreadpool(String threadpool) {
            this.threadpool = threadpool;
        }

        public Integer getThreads() {
            return threads;
        }

        public void setThreads(Integer threads) {
            this.threads = threads;
        }

        public Integer getQueues() {
            return queues;
        }

        public void setQueues(Integer queues) {
            this.queues = queues;
        }

        public String getSerialization() {
            return serialization;
        }

        public void setSerialization(String serialization) {
            this.serialization = serialization;
        }

        public Duration getTimeout() {
            return timeout;
        }

        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }

        public Integer getConnections() {
            return connections;
        }

        public void setConnections(Integer connections) {
            this.connections = connections;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }

    /**
     * 服务提供者配置
     */
    public static class Provider {
        /**
         * 超时时间
         */
        private Duration timeout = Duration.ofSeconds(10);

        /**
         * 重试次数
         */
        private Integer retries = 0;

        /**
         * 负载均衡策略
         */
        private String loadbalance = "random";

        /**
         * 集群策略
         */
        private String cluster = "failover";

        /**
         * 权重
         */
        private Integer weight = 100;

        /**
         * 分组
         */
        private String group;

        /**
         * 版本
         */
        private String version;

        /**
         * 服务参数
         */
        private Map<String, String> parameters;

        public Duration getTimeout() {
            return timeout;
        }

        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }

        public Integer getRetries() {
            return retries;
        }

        public void setRetries(Integer retries) {
            this.retries = retries;
        }

        public String getLoadbalance() {
            return loadbalance;
        }

        public void setLoadbalance(String loadbalance) {
            this.loadbalance = loadbalance;
        }

        public String getCluster() {
            return cluster;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }

    /**
     * 服务消费者配置
     */
    public static class Consumer {
        /**
         * 超时时间
         */
        private Duration timeout = Duration.ofSeconds(10);

        /**
         * 重试次数
         */
        private Integer retries = 0;

        /**
         * 负载均衡策略
         */
        private String loadbalance = "random";

        /**
         * 集群策略
         */
        private String cluster = "failover";

        /**
         * 分组
         */
        private String group;

        /**
         * 版本
         */
        private String version;

        /**
         * 是否检查服务存在
         */
        private Boolean check = false;

        /**
         * 是否启用异步调用
         */
        private Boolean async = false;

        /**
         * 服务参数
         */
        private Map<String, String> parameters;

        public Duration getTimeout() {
            return timeout;
        }

        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }

        public Integer getRetries() {
            return retries;
        }

        public void setRetries(Integer retries) {
            this.retries = retries;
        }

        public String getLoadbalance() {
            return loadbalance;
        }

        public void setLoadbalance(String loadbalance) {
            this.loadbalance = loadbalance;
        }

        public String getCluster() {
            return cluster;
        }

        public void setCluster(String cluster) {
            this.cluster = cluster;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Boolean getCheck() {
            return check;
        }

        public void setCheck(Boolean check) {
            this.check = check;
        }

        public Boolean getAsync() {
            return async;
        }

        public void setAsync(Boolean async) {
            this.async = async;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }
    }
}
