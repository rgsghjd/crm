package cg.software.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler {
    private Object target;
    public MyInvocationHandler(Object target){
        this.target=target;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object obj=null;
        SqlSession session=null;
        try{
            System.out.println("进入invoke");
            session=SqlSessionUtil.getSqlSession();
            obj=method.invoke(target,args);
            session.commit();

        }catch (Exception e){
            session.rollback();
            e.printStackTrace();
            //保持目标类的错误上抛
            throw e.getCause();

        }finally {
            SqlSessionUtil.myClose(session);
        }
        return obj;
    }
    public Object getProxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }
}
