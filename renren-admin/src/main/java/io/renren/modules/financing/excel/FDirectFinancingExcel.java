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
 * 直接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FDirectFinancingExcel {
    @ExcelProperty(value = "融资单位", index = 0)
    private String deptName;
    @ExcelProperty(value = "批文号", index = 1)
    private String approveNo;
    @ExcelProperty(value = "审批金额", index = 2)
    private BigDecimal approvalAmount;
    @ExcelProperty(value = "融资品种", index = 3)
    private String varieties;
    @ExcelProperty(value = "发行外币金额", index = 4)
    private BigDecimal foreignCurrencyAmount;
    @ExcelProperty(value = "折合人民币", index = 5)
    private BigDecimal issueQuota;
    @ExcelProperty(value = "利率", index = 6)
    private String rate;
    @ExcelProperty(value = "发行日", index = 7)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date issueDate;
    @ExcelProperty(value = "起息日", index = 8)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date valueDate;
    @ExcelProperty(value = "到期日", index = 9)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date dueDate;
    @ExcelProperty(value = "增信措施", index = 10)
    private String creditMeasures;
}