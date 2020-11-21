package com.cp3.cloud.database.p6spy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cp3.cloud.context.BaseContextHandler;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * 用于 p6spy 在输出的sql日志中，打印当前租户、当前用户ID、当前数据源连接url
 *
 * @author cp3
 * @date 2020/9/3 3:39 下午
 */
public class TenantP6SpyLogger implements MessageFormattingStrategy {
    public static final String REGX = "[\\s]+";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ?
                StrUtil.format(" tenant: {} userId: {} \n Consume Time：{} ms {} \n url: {} \n Execute SQL：{} \n",
                        BaseContextHandler.getTenant(), BaseContextHandler.getUserId(), elapsed, now, url, sql.replaceAll(REGX, StringPool.SPACE)) :
                StringPool.EMPTY;
    }
}