package com.nju.pams.util.constant;

/**
 * @author yangyueyang <br>
 * date 2017-03-18
 */
public enum ResultEnum {
	
	/* 作为服务端返回json数据的结果
	 * enum不提供setter方法
	 */
	
    Success(0, "成功"),
    
    //参数校验错误 1--9
    NullParameter(1, "参数存在空值"),
    
    
    //验证码相关错误 11--19
    ErrorVerificationCode(11, "验证码输入错误"),
    
    //用户信息相关错误 21--29
    DuplicateUsername(21, "该用户名已经存在"),
    UsernameNotExist(22, "该用户名找不到对应的用户信息"),
    
    //session已经结束
    SessionClose(31, "会话已经结束，用户请重新登录"),
    
    
    //完全结转存款错误
    CloseDepositError(41, "结转存款错误"),
    CloseLoanError(42, "结束贷款错误"),
    
    DeleteLoanChangeError(51, "还款记录中不能直接删除创建贷款的记录"),
    
    //用户图片的相关错误
    UploadPictureError(61, "上传图片失败"),
    PictureNameTooLong(62, "用户图片名称过长"),
    PictureTypeError(63, "请选择以png／jpg／jpeg结尾的图片"),
    
    //用户持仓相关的错误
    DeleteStockChangeError(71, "删除交易记录失败，请检查交易记录的时间以及相关信息"),
    
    //用户反馈相关的错误
    FeedbackError(81, "用户反馈处理错误"),
    
    //公告错误
    NoticeError(91, "公告处理错误"),
    
    
    UnknownError(99, "未知错误");

    private final int code;
    private final String msg;

    ResultEnum(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public final int getCode() {
        return code;
    }

    public final String getMsg() {
        return msg;
    }
}