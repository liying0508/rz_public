package io.renren.modules.financing.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class UnitCastUtil {

    /**
     * BigDecimal类型的除10000并保留两位小数的运算
     */
    public static BigDecimal divider(BigDecimal bigDecimal){
        if (bigDecimal == null) {
            return null;
        }
        double a=bigDecimal.doubleValue();
        a=a/10000;
        String numString="0.";
        for(int i=0;i<2;i++){
            numString+="0";
        }
        DecimalFormat df=new DecimalFormat(numString);
        return new BigDecimal(df.format(a).toString());
    }

    /**
     * BigDecimal类型的乘10000并保留两位小数的运算
     */
    public static BigDecimal multiplication(BigDecimal bigDecimal){
        if (bigDecimal == null) {
            return null;
        }
        double a=bigDecimal.doubleValue();
        a = a*10000;
        String b = String.valueOf(a);
        return new BigDecimal(b);
    }
}
