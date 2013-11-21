package com.hik.RtspClient.Exception;

public class NetException extends Exception {
    /** 变量/常量说明 */
    private static final long serialVersionUID = 1L;
    private static final int NO_ERROR = 0;
    /** 错误码 */
    private int mErrorCode = NO_ERROR;

    public NetException(String des, int errCode) {
        // TODO Auto-generated constructor stub
        super(des);
        mErrorCode = errCode;
    }
}
