package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Eddy
 * @Date: 2022/11/11
 * @Time: 16:00 
 * @Description:
 */

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FRepaymentInfoExcel {
    @ExcelProperty(value = "融资单位", index = 0)
    private String deptName;
    @ExcelProperty(value = "金融机构名称", index = 1)
    private String institutionName;
    @ExcelProperty(value = "融资品种", index = 2)
    private String varieties;
    @ExcelProperty(value = "提款金额", index = 3)
    private BigDecimal totalAmount;
    @ExcelProperty(value = "已还本金", index = 4)
    private String principalRepay;
    @ExcelProperty(value = "未还本金", index = 5)
    private String outstandingPrincipal;
    @ExcelProperty(value = "已付利息", index = 6)
    private String interestRepay;

}
