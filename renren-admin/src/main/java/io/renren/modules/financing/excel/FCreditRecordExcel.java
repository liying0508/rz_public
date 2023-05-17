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
 * 银行授信
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FCreditRecordExcel {
    @ExcelProperty(value = "授信单位", index = 0)
    private String deptName;
    @ExcelProperty(value = "金融机构类型", index = 1)
    private String institutionType;
    @ExcelProperty(value = "金融机构", index = 2)
    private String institutionName;
    @ExcelProperty(value = "授信额度", index = 3)
    private BigDecimal quota;
    @ExcelProperty(value = "起始日", index = 4)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date startDate;
    @ExcelProperty(value = "到期日", index = 5)
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;
    @ExcelProperty(value = "增信措施", index = 6)
    private String creditMeasures;
    @ExcelProperty(value = "已使用额度", index = 7)
    private BigDecimal indirectQuota;
    @ExcelProperty(value = "未使用额度", index = 8)
    private BigDecimal unUsedQuota;
}