/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.common.exception;

/**
 * 错误编码，由5位数字组成，前2位为模块编码，后3位为业务编码
 * <p>
 * 如：10001（10代表系统模块，001代表业务代码）
 * </p>
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public interface ErrorCode {
    int INTERNAL_SERVER_ERROR = 500;
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;

    int NOT_NULL = 10001;
    int DB_RECORD_EXISTS = 10002;
    int PARAMS_GET_ERROR = 10003;
    int ACCOUNT_PASSWORD_ERROR = 10004;
    int ACCOUNT_DISABLE = 10005;
    int IDENTIFIER_NOT_NULL = 10006;
    int CAPTCHA_ERROR = 10007;
    int SUB_MENU_EXIST = 10008;
    int PASSWORD_ERROR = 10009;
    int ACCOUNT_NOT_EXIST = 10010;
    int SUPERIOR_DEPT_ERROR = 10011;
    int SUPERIOR_MENU_ERROR = 10012;
    int DATA_SCOPE_PARAMS_ERROR = 10013;
    int DEPT_SUB_DELETE_ERROR = 10014;
    int DEPT_USER_DELETE_ERROR = 10015;
    int ACT_DEPLOY_ERROR = 10016;
    int ACT_MODEL_IMG_ERROR = 10017;
    int ACT_MODEL_EXPORT_ERROR = 10018;
    int UPLOAD_FILE_EMPTY = 10019;
    int TOKEN_NOT_EMPTY = 10020;
    int TOKEN_INVALID = 10021;
    int ACCOUNT_LOCK = 10022;
    int ACT_DEPLOY_FORMAT_ERROR = 10023;
    int OSS_UPLOAD_FILE_ERROR = 10024;
    int SEND_SMS_ERROR = 10025;
    int MAIL_TEMPLATE_NOT_EXISTS = 10026;
    int REDIS_ERROR = 10027;
    int JOB_ERROR = 10028;
    int INVALID_SYMBOL = 10029;
    int JSON_FORMAT_ERROR = 10030;
    int SMS_CONFIG = 10031;
    int TASK_CLIME_FAIL = 10032;
    int NONE_EXIST_PROCESS = 10033;
    int SUPERIOR_NOT_EXIST = 10034;
    int REJECT_MESSAGE = 10035;
    int ROLLBACK_MESSAGE = 10036;
    int UNCLAIM_ERROR_MESSAGE = 10037;
    int SUPERIOR_REGION_ERROR = 10038;
    int REGION_SUB_DELETE_ERROR = 10039;
    int PROCESS_START_ERROR = 10040;
    int REJECT_PROCESS_PARALLEL_ERROR = 10041;
    int REJECT_PROCESS_HANDLEING_ERROR = 10042;
    int END_PROCESS_PARALLEL_ERROR = 10043;
    int END_PROCESS_HANDLEING_ERROR = 10044;
    int END_PROCESS_MESSAGE = 10045;
    int BACK_PROCESS_PARALLEL_ERROR = 10046;
    int BACK_PROCESS_HANDLEING_ERROR = 10047;

    //无直融批文 20
    int APPROVAL_NO_INFO=20000;

    int APPROVAL_NO_EXI_INFO=20001;
    //存在有关联的直融信息
    int DELETE_ERROR_EXIST_RELATED_DIRECT_FINANCE = 20002;
    //融资授信 30

    //直接融资 40

    //间接融资 50
    //间融信息缺失 50001
    int FINANCING_INFO_GAP = 50001;
    //间融审核时的额判断（是否存在该授信）
    int CREDIT_RECORD_IS_NOT_EXIST = 50002;
    //间融审核时的判断（授信是否通过）
    int CREDIT_RECORD_CHECKED_STATUS_ERROR = 50003;
    //间融审核时的判断（授信额度是否足够）
    int CREDIT_QUOTA_IS_NOT_ENOUGH = 500004;

    //还款计划和实际还款 60
    //60000 还款系统出错
    int REPAYMENT_INFO_SYS_ERROR = 60000;
    //60001 尚未保存该笔信息
    int FINANCE_INFO_NOT_SAVED_YET = 60001;
    //60002 实际还款中还本的总额大于提取的本金
    int REPAYMENT_PRINCIPAL_TO_BIG_ERROR = 60002;

    //利息测算 70
    int REPAYMENT_INFO_MISSING_REPAY_DATE = 70001;


    //授信总额 80
    //80001 授信总额的金融机构重复0
    int CREDIT_QUOTA_ERROR_INSTITUTION_IS_REPETITIVE = 80001;
}
