package JDBCUtils;

import bean.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils
{
    public static void main(String[] args) throws Exception
    {
//        JDBCUtils.InsertUserByNameAndPsw(new User("洛琪希","123456"));
    }
//    private static Connection con;
//    static
//    {
//        try
//        {
//            con = getConnection();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    static private Connection getConnection() throws Exception
    {
        InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("ServerConfig");
        Properties pros = new Properties();
        pros.load(is);
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");
        Class.forName(driverClass);
        Connection con = DriverManager.getConnection(url,user,password);
        return con;
    }

    static public User getUserByNameAndPsw(User user ) throws Exception
    {
        User res = null;

        Connection con = JDBCUtils.getConnection();

        String sql  = "select * from user where username = ? and password = ? ;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,user.getUsername());
        ps.setObject(2,user.getPassword());
        ResultSet rs = ps.executeQuery();
        System.out.println(rs);
//        ResultSetMetaData md = rs.getMetaData();
//        System.out.println(md.getColumnCount());
        if( rs.next() )
        {
            Object object1 = rs.getObject(2);
            Object object2 = rs.getObject(3);
            res = new User((String)object1,(String)object2);
        }
        con.close();
        return res;
    }

    static public boolean getUserByName( String username ) throws Exception
    {
        Connection con = getConnection();
        String sql = "select * from user where username = ? ;";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1,username);
        ResultSet rs = ps.executeQuery();

        if( rs.next() )
        {
            con.close();
            return true;
        }
        con.close();
        return false;
    }

    static synchronized public boolean InsertUserByNameAndPsw(User user) throws Exception {
        Connection con = null;
        con = getConnection();
        try
        {

            String sql = "insert into user(username,password) values( ? , ? )";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1,user.getUsername());
            ps.setObject(2,user.getPassword());
            ps.execute();
            ps.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
            con.close();
            return false;
        }
        con.close();
        return true;
    }

}
