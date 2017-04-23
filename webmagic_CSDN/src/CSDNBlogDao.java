import java.sql.*;

/**
 * Created by 11981 on 2017/4/21.
 */
public class CSDNBlogDao{
    // JDBC 驱动器名称 和数据库地址
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/sampledb?" +
            "useUnicode=true&characterEncoding=utf-8&useSSL=false";
    //加这一行可以消除一个warning
    //warning:建立ssl连接，但是服务器没有身份认证，这种方式不推荐使用

    //  数据库用户和密码
    static final String USER = "root";
    static final String PASSWORD = "root";

    private Connection conn = null;
    private Statement stat = null;

    public CSDNBlogDao(){

        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASSWORD);
            stat = conn.createStatement();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public int add(CSDNBlog csdnBlog){
        try{
            String sql = "INSERT INTO `sampledb`.`csdnblog` (`id`, `title`, `date`, `tags`, `categories`, `view`, `comments`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, csdnBlog.getId());
            ps.setString(2, csdnBlog.getTitle());
            ps.setString(3, csdnBlog.getDate());
            ps.setString(4, csdnBlog.getTags());
            ps.setString(5, csdnBlog.getCategories());
            ps.setInt(6, csdnBlog.getView());
            ps.setInt(7, csdnBlog.getComments());
            return ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if (stat != null){
                    stat.close();
                }
            }catch (SQLException se){

            }

            try{
                if (conn != null){
                    conn.close();
                }
            }catch (SQLException se2){

            }
        }
        return -1;
    }

}
