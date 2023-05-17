package io.renren.modules.sys.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@ContentRowHeight(20)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SysSecComExcel {

    @ExcelProperty(value = "证券公司名", index = 0)
    private String secComName;

    @ExcelProperty(value = "辖区", index = 1)
    private String area;
}
