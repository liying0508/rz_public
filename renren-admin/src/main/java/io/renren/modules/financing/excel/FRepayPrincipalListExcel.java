package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @Author: Eddy
 * @Date: 2022/11/14 0014
 * @Time: 9:30
 * @Description: 还本计划的列表excel
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FRepayPrincipalListExcel {

    @ExcelProperty(value = "还款日期", index = 0)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;
    @ExcelProperty(value = "偿还本金", index = 1)
    private BigDecimal repaymentOfPrincipal;
}
