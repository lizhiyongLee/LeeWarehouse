package com.ils.modules.mes.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;

/**
 * 数字工具类
 *
 * @author niushuai
 * @date 2021/11/8 17:51:13
 */
public class NumUtil {

    public static Double avg(String sum, int count, int scale) {

        return avg(sum, count, scale, RoundingMode.HALF_UP);
    }

    public static Double avg(String sum, int count, int scale, RoundingMode roundingMode) {

        BigDecimal decimal = BigDecimal.valueOf(Double.valueOf(sum) / count).setScale(scale, roundingMode);

        return decimal.doubleValue();
    }

    public static Double avg(Collection<? super Number> totals, int scale) {
        return avg(totals, scale, RoundingMode.HALF_UP);
    }

    public static Double avg(Collection<? super Number> totals, int scale, RoundingMode roundingMode) {

        double sum = totals.stream().mapToDouble(item -> Double.parseDouble(String.valueOf(item))).sum();

        BigDecimal decimal = BigDecimal.valueOf(sum / totals.size()).setScale(scale, roundingMode);

        return decimal.doubleValue();
    }

    public static Double sum(Collection<? super Double> totals) {
        return totals.stream().mapToDouble(item -> Double.parseDouble(String.valueOf(item))).sum();
    }
}
