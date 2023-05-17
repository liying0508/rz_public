package io.renren.modules.financing.vo;

import io.renren.modules.financing.dto.RepaymentInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Eddy
 * @Date: 2022/9/7
 * @Time: 16:56
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepaymentInfoVO {
    private List<RepaymentInfoDTO> repayInterestInfoList;

    private List<RepaymentInfoDTO> repayPrincipalInfoList;
}
