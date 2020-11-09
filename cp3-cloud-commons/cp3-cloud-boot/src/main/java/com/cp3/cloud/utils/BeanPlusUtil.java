package com.cp3.cloud.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean增强类工具类
 * @see https://www.bookstack.cn/read/hutool/374babce0b75a069.md
 * 深拷贝：是一个整个独立的对象拷贝，深拷贝会拷贝所有的属性,并拷贝属性指向的动态分配的内存。当对象和它所引用的对象一起拷贝时即发生深拷贝。深拷贝相比于浅拷贝速度较慢并且花销较大
 * 浅拷贝：浅拷贝仅仅复制所考虑的对象，而不复制它所引用的对象。可引入：Dozer
 * <p>
 * 把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。
 * </p>
 *
 * @author cp3
 * @since 3.1.2
 */
public class BeanPlusUtil extends BeanUtil {

    /**
     * 转换 list
     *
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<T> toBeanList(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
            return Collections.emptyList();
        }
        return sourceList.parallelStream()
                .filter(item -> item != null)
                .map((source) -> toBean(source, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * 转化Page 对象
     *
     * @param page
     * @param destinationClass
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> IPage<T> toBeanPage(IPage<E> page, Class<T> destinationClass) {
        if (page == null || destinationClass == null) {
            return null;
        }
        IPage<T> newPage = new Page<>(page.getCurrent(), page.getSize());
        newPage.setPages(page.getPages());
        newPage.setTotal(page.getTotal());

        List<E> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return newPage;
        }

        List<T> destinationList = toBeanList(list, destinationClass);

        newPage.setRecords(destinationList);
        return newPage;
    }

}
