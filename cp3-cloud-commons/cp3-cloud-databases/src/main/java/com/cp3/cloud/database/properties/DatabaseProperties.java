package com.cp3.cloud.database.properties;

import com.baidu.fsg.uid.buffer.RejectedPutBufferHandler;
import com.baidu.fsg.uid.buffer.RejectedTakeBufferHandler;
import com.baidu.fsg.uid.buffer.RingBuffer;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cp3.cloud.database.properties.DatabaseProperties.PREFIX;
import static com.cp3.cloud.database.properties.MultiTenantType.SCHEMA;


/**
 * 客户端认证配置
 *
 * @author cp3
 * @date 2018/11/20
 */
@ConfigurationProperties(prefix = PREFIX)
@Data
@NoArgsConstructor
public class DatabaseProperties {
    public static final String PREFIX = "zuihou.database";
    /**
     * 是否启用 防止全表更新与删除插件
     *
     * @return
     */
    public Boolean isBlockAttack = false;
    /**
     * 是否启用  sql性能规范插件
     */
    public Boolean isIllegalSql = false;
    /**
     * 是否启用 seata
     */
    public Boolean isSeata = false;
    /**
     * 分页大小限制
     */
    protected long limit = -1;

    protected DbType dbType = DbType.MYSQL;
    /**
     * 是否禁止写入
     */
    private Boolean isNotWrite = false;
    /**
     * 是否启用数据权限
     */
    private Boolean isDataScope = true;
    /**
     * 事务超时时间
     */
    private int txTimeout = 60 * 60;

    /**
     * 租户库 前缀
     */
    private String tenantDatabasePrefix = "zuihou_base";

    /**
     * 多租户模式
     */
    private MultiTenantType multiTenantType = SCHEMA;
    /**
     * 租户id 列名
     */
    private String tenantIdColumn = "tenant_code";
    /**
     * id 类型
     */
    private IdType idType = IdType.HUTOOL;
    /**
     * 百度 uid 生成策略
     */
    private CacheId cacheId = new CacheId();
    private DefaultId defaultId = new DefaultId();
    private HutoolId hutoolId = new HutoolId();

    /**
     * 统一管理事务的方法名
     */
    private List<String> transactionAttributeList = new ArrayList<>(Arrays.asList("add*", "save*", "insert*",
            "create*", "update*", "edit*", "upload*", "delete*", "remove*",
            "clean*", "recycle*", "batch*", "mark*", "disable*", "enable*", "handle*", "syn*",
            "reg*", "gen*", "*Tx"
    ));
    /**
     * 事务扫描基础包
     */
    private String transactionScanPackage = "com.cp3.cloud";

    @Data
    public static class HutoolId {
        /**
         * 终端ID (0-31)      单机配置0 即可。 集群部署，根据情况每个实例自增即可。
         */
        private Long workerId = 0L;
        /**
         * 数据中心ID (0-31)   单机配置0 即可。 集群部署，根据情况每个实例自增即可。
         */
        private Long dataCenterId = 0L;
    }

    /**
     * 参数说明参考：https://github.com/baidu/uid-generator
     * 长期运行：重启频率为12次/天, 那么配置成{"workerBits":23,"timeBits":31,"seqBits":9}时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年
     * 长期运行+集群数量少+并发稍高：{"workerBits":20,"timeBits":31,"seqBits":12}这样的配置可以68年，100W次重启，单机每秒4096个并发的情况
     * 频繁重启：重启频率为24*12次/天, 那么配置成{"workerBits":27,"timeBits":30,"seqBits":6}时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.
     */
    @Data
    public static class CacheId extends DefaultId {

        /**
         * RingBuffer size扩容参数, 可提高UID生成的吞吐量.
         * 默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
         */
        private int boostPower = 3;
        /**
         * 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
         * 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
         * 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
         */
        private int paddingFactor = RingBuffer.DEFAULT_PADDING_PERCENT;
        /**
         * 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
         * 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
         */
        private Long scheduleInterval = 5 * 60L;
        /**
         * 拒绝策略: 当环已满, 无法继续填充时
         * 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
         */
        private Class<? extends RejectedPutBufferHandler> rejectedPutBufferHandlerClass;
        /**
         * 拒绝策略: 当环已空, 无法继续获取时
         * 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式)
         */
        private Class<? extends RejectedTakeBufferHandler> rejectedTakeBufferHandlerClass;
    }

    @Data
    public static class DefaultId {

        /**
         * 当前时间，相对于时间基点"${epochStr}"的增量值，单位：秒，
         * 28: 大概可以使用 8.7年, 28位即最大表示2^28的数值的秒数
         * 30: 大概可以使用 34年, 30位即最大表示2^30的数值的秒数
         * 31: 大概可以使用 68年, 31位即最大表示2^31的数值的秒数
         */
        protected int timeBits = 31;
        /**
         * 机器id，
         * <p>
         * 20：100W次重启
         * 22: 最多可支持约420w次机器启动。内置实现为在启动时由数据库分配。420w = 2^22
         * 23：800w次重启  12次/天
         * 27: 1.3亿次重启 24*12次/天
         */
        protected int workerBits = 23;
        /**
         * 每秒下的并发序列，13 bits可支持每秒8192个并发，即2^13个并发
         * 9: 512 并发
         * 13: 8192 并发
         */
        protected int seqBits = 9;
        /**
         * Customer epoch, unit as second. For example 2016-05-20 (ms: 1463673600000)
         * 可以改成你的项目开始开始的时间
         */
        protected String epochStr = "2020-09-15";
    }

}
