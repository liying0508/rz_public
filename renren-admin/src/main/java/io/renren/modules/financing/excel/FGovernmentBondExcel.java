package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 政府债券
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FGovernmentBondExcel {
    @ExcelProperty(value = "融资品种", index = 0)
    private String varieties;
    @ExcelProperty(value = "融资金额", index = 1)
    private BigDecimal financingAmount;
    @ExcelProperty(value = "利率（%）", index = 2)
    private double rate;
    @ExcelProperty(value = "放款日期", index = 3)
    private String loanDate;
    @ExcelProperty(value = "还款日期", index = 4)
    private String repaymentDate;
    @ExcelProperty(value = "融资余额", index = 5)
    private BigDecimal financingBalance;
    @ExcelProperty(value = "项目", index = 6)
    private String project;
    @ExcelProperty(value = "付息方式", index = 7)
    private String interestMethod;
    @ExcelProperty(value = "还本方式", index = 8)
    private String principalMethod;
    @ExcelProperty(value = "增信措施", index = 9)
    private String creditMeasures;
}