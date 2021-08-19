package cg.software.settings.Exception;

public class MyException extends Exception {
    /**
     * 自定义异常类，用于用户登陆时错误信息获取
     * @param mes
     */
    public MyException(String mes){
        super(mes);
    }
}
