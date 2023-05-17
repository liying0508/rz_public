package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 间接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FIndirectFinancingExcel {
    @ExcelProperty(value = "融资主体", index = 0)
    private String deptName;
    @ExcelProperty(value = "金融机构类型", index = 1)
    private String institutionType;
    @ExcelProperty(value = "金融机构", index = 2)
    private String institutionName;
    @ExcelProperty(value = "融资品种", index = 3)
    private String varieties;
    @ExcelProperty(value = "保证金比例", index = 4)
    private Integer marginRatio;
    @ExcelProperty(value = "贴现率", index = 5)
    private Integer discountRate;
    @ExcelProperty(value = "利率类型", index = 6)
    private String interestRatesWay;
//    @ExcelProperty(value = "浮动利率周期", index = 7)
//    private Integer lprCycle;
    @ExcelProperty(value = "利率（%）", index = 7)
    private String rate;
    @ExcelProperty(value = "合同编号", index = 8)
    private String contractNo;
    @ExcelProperty(value = "合同金额", index = 9)
    private BigDecimal contractAmount;
    @ExcelProperty(value = "开票金额", index = 10)
    private BigDecimal invoiceAmount;
    @ExcelProperty(value = "提款金额", index = 11)
    private BigDecimal withdrawalAmount;
    @ExcelProperty(value = "付息方式", index = 12)
    private String interestMethod;
    @ExcelProperty(value = "放款日期", index = 13)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date loanDate;
    @ExcelProperty(value = "期限（月）", index = 14)
    private Long deadLine;
    @ExcelProperty(value = "还款日期", index = 15)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repaymentDate;
    @ExcelProperty(value = "可用余额", index = 16)
    private BigDecimal financingBalance;
    @ExcelProperty(value = "其他费用", index = 17)
    private String otherExpenses;
    @ExcelProperty(value = "用途", index = 18)
    private String purpose;
    @ExcelProperty(value = "增信措施", index = 19)
    private String creditMeasures;
    @ExcelProperty(value = "决策依据", index = 20)
    private String decisionBasis;
    @ExcelProperty(value = "备注", index = 21)
    private String remarks;
}