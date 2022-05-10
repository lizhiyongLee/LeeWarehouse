package com.ils.modules.mes.util;

import java.util.Comparator;

/**
 * 比较器类
 *
 * @author Anna.
 * @date 2021/7/1 11:48
 */
public class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareTo(o2);
    }
}
