package com.lanfeng.gupai.utils.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DoubleUtil {
    protected static final Log log = LogFactory.getLog(DoubleUtil.class);
    private static final double esp = 0.00000002;

    public static Double parse(Object value) {
        if (value == null || value.toString().isEmpty()) {
            return Double.NaN;
        } else if (value instanceof Integer || value instanceof Long) {
            return Double.parseDouble(value.toString());
        } else if (value instanceof Double || value instanceof Float) {
            return (Double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                log.debug("Invalid number format: " + value);
            }
        }

        return Double.NaN;
    }

    public static boolean isZero(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            return false;
        }
        return (Math.abs(v - 0.0) < esp);
    }

    public static Double parse(double value) {
        return value;
    }

    public static double round(double value, int decimalPlace) {
        if (!isValid(value) || decimalPlace < 0 || decimalPlace >= 6) {
            return Double.NaN;
        }

        long scale = 1;
        for (int i = 0; i < decimalPlace; i++) {
            scale *= 10;
        }

        double ep = (value < 0 ? -0.5 : 0.5);
        return (double) (long) (value * scale + ep) / scale;
    }

    public static boolean isValid(Double value) {
        return value != null && !Double.isNaN(value) && !Double.isInfinite(value);
    }

    public static boolean isValid(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    /**
     * Test whether value 1 is equal to value 2, the difference is less than 00000002, assume there are equal
     * 
     * @param value1
     *            : value 1
     * @param value2
     *            : value 2
     * @return true if value1 is equal to value2. If not, return false.
     */
    public static boolean isEqual(double value1, double value2) {
        if (Math.abs(value1 - value2) < esp) {
            return true;
        }
        return false;
    }

    public static boolean isLessThan(double value1, double value2) {
        if ((value2 - value1) > esp) {
            return true;
        }

        return false;
    }

    public static boolean isLessThanOrEqual(double value1, double value2) {
        if ((value2 - value1) > esp || Math.abs(value1 - value2) < esp) {
            return true;
        }
        return false;
    }

    public static boolean isMoreThan(double value1, double value2) {
        if ((value1 - value2) > esp) {
            return true;
        }
        return false;
    }

    public static boolean isMoreThanOrEqual(double value1, double value2) {
        if ((value1 - value2) > esp || Math.abs(value1 - value2) < esp) {
            return true;
        }
        return false;
    }

    public static Double add(Double d1, Double d2) {
        if (!isValid(d1) || !isValid(d2)) {
            return Double.NaN;
        } else {
            return d1 + d2;
        }
    }

    public static Double subtract(Double d1, Double d2) {
        if (!isValid(d1) || !isValid(d2)) {
            return Double.NaN;
        } else {
            return d1 - d2;
        }
    }

    public static Double multiply(Double d1, Double d2) {
        if (!isValid(d1) || !isValid(d2)) {
            return Double.NaN;
        } else {
            return d1 * d2;
        }
    }

    public static Double divide(Double d1, Double d2) {
        if (!isValid(d1) || !isValid(d2) || d2 == 0) {
            return Double.NaN;
        } else {
            return d1 / d2;
        }
    }

    public static Double add(Object p1, Object p2) {
        if (p1 == null || p2 == null) {
            return Double.NaN;
        }

        Double d1 = DoubleUtil.parse(p1);
        Double d2 = DoubleUtil.parse(p2);
        return DoubleUtil.add(d1, d2);
    }

    public static Double subtract(Object p1, Object p2) {
        if (p1 == null || p2 == null) {
            return Double.NaN;
        }

        Double d1 = DoubleUtil.parse(p1);
        Double d2 = DoubleUtil.parse(p2);
        return DoubleUtil.subtract(d1, d2);
    }

    public static Double multiply(Object p1, Object p2) {
        if (p1 == null || p2 == null) {
            return Double.NaN;
        }

        Double d1 = DoubleUtil.parse(p1);
        Double d2 = DoubleUtil.parse(p2);
        return DoubleUtil.multiply(d1, d2);
    }

    public static Double divide(Object p1, Object p2) {
        if (p1 == null || p2 == null) {
            return Double.NaN;
        }

        Double d1 = DoubleUtil.parse(p1);
        Double d2 = DoubleUtil.parse(p2);
        return DoubleUtil.divide(d1, d2);
    }

    public static Object toJSON(Double value) {
        if (isValid(value)) {
            return value;
        } else {
            return "NaN";
        }
    }

    public static Object toJSON(double value) {
        if (isValid(value)) {
            return value;
        } else {
            return "NaN";
        }
    }

    public static Object toJSONWithQuoteForString(double value) {
        if (isValid(value)) {
            return value;
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        double d1 = Double.POSITIVE_INFINITY;
        double d2 = Double.NEGATIVE_INFINITY;
        double d3 = Double.NaN;
        double d4 = 1.0;

        System.out.println(isValid(d1));
        System.out.println(isValid(d2));
    }
}
