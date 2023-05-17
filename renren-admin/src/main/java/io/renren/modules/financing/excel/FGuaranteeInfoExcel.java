package io.renren.modules.financing.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import java.util.Date;

/**
 * 担保信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class FGuaranteeInfoExcel {
    @ExcelProperty(value = "Integer", index = 0)
    private Integer id;
    @ExcelProperty(value = "授信单ID", index = 1)
    private Long creditId;
    @ExcelProperty(value = "信用", index = 2)
    private String creditText;
    @ExcelProperty(value = "保证人", index = 3)
    private String guarantor;
    @ExcelProperty(value = "比例", index = 4)
    private Integer ratio;
    @ExcelProperty(value = "合同金额", index = 5)
    private Integer contractAmount;
    @ExcelProperty(value = "抵押物名称", index = 6)
    private String collateralName;
    @ExcelProperty(value = "抵押物评估值", index = 7)
    private Integer collateralAmount;
    @ExcelProperty(value = "抵押合同", index = 8)
    private String collateralContract;
    @ExcelProperty(value = "解押时间", index = 9)
    private Date releaseTime;
    @ExcelProperty(value = "质押标的", index = 10)
    private String pledgeType;
    @ExcelProperty(value = "质押人", index = 11)
    private String pledgeName;
    @ExcelProperty(value = "质押价值", index = 12)
    private Integer pledgeAmount;
    @ExcelProperty(value = "备注", index = 13)
    private String remarks;
    @ExcelProperty(value = "创建者", index = 14)
    private Long creator;
    @ExcelProperty(value = "创建时间", index = 15)
    private Date createDate;
}