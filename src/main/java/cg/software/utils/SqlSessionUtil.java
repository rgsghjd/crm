package cg.software.utils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    private static SqlSessionFactory sqlSessionFactory=null;
    private SqlSessionUtil(){};
    static {
        String resource="mybatis.xml";
        try {
            InputStream fi= Resources.getResourceAsStream(resource);
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder=new SqlSessionFactoryBuilder();
            sqlSessionFactory=sqlSessionFactoryBuilder.build(fi);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static ThreadLocal<SqlSession> t = new ThreadLocal<SqlSession>();
    public static SqlSession getSqlSession(){
        SqlSession session = t.get();
        if(session==null){
             session=sqlSessionFactory.openSession();
            t.set(session);
        }

        return session;
    }

    public  static void myClose(SqlSession sqlSession){

        if(sqlSession != null){
            sqlSession.close();
            t.remove();
        }
    }
}
