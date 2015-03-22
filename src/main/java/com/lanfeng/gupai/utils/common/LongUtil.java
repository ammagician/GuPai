package com.lanfeng.gupai.utils.common;

public class LongUtil {
    public static long parse(Object value) {
        if (value == null) {
            return Long.MIN_VALUE;
        } else if (value.getClass() == Long.class) {
            return (Long) value;
        } else if (value.getClass() == String.class) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (value.getClass() == Double.class) {
            try {
                Double val = (Double) value + 0.5;
                return val.longValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return Long.MIN_VALUE;
    }
}
