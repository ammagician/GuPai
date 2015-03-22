package com.lanfeng.gupai.exception;

public class GPException extends Exception {
    private static final long serialVersionUID = 5452143825736069154L;

    // for BusinessException, error code is positive, it is businessCode
    // for other exceptions, error code is negative
    public enum Type {
        InvalidUser, InvalidOwner, CalculationDateChanged, SocketTimeoutError, MssError, UnknownError, FileNotFound, IO, FTP, JsonFormat, UnknownHost, MongoException, BusinessException, DataIssue, NoHoldings, RtqError, EndDateIsZero, NoWidgetSetting, NoPortfolioPermission, ColumnSet, WrongSecurityToken, WrongExcelFormat
    };

    private Type type = null;
    private int businessCode = 0;

    public GPException(Type type) {
        super();
        this.type = type;
    }

    public GPException(Type type, int businessCode) {
        super();
        this.type = type;
        this.businessCode = businessCode;
    }

    public GPException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public GPException(Type type, Throwable t) {
        super(t);
        this.type = type;
    }

    public GPException(Type type, String message, Throwable t) {
        super(message, t);
        this.type = type;
    }

    public GPException(Type type, int businessCode, Throwable t) {
        super(t);
        this.type = type;
        this.businessCode = businessCode;
    }

    public Type getType() {
        return this.type;
    }

    public int getBusinessCode() {
        return businessCode;
    }
}
