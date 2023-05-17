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

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FRepaymentInfoInterestListExcel {
    @ExcelProperty(value = "已还利息", index = 0)
    private BigDecimal alreadyRepayInterest;
    @ExcelProperty(value = "还款日期", index = 1)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repayDate;
}
