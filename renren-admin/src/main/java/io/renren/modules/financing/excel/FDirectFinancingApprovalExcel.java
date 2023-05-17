package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FDirectFinancingApprovalExcel {

    @ExcelProperty(value = "融资单位", index = 0)
    private String deptName;
    @ExcelProperty(value = "审批单位", index = 1)
    private String approveOrg;
    @ExcelProperty(value = "批文号", index = 2)
    private String approveNo;
    @ExcelProperty(value = "币种", index = 3)
    private String currency;
    @ExcelProperty(value = "审批金额", index = 4)
    private BigDecimal issueQuota;

}
