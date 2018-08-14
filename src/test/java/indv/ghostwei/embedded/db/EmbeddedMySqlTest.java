package indv.ghostwei.embedded.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmbeddedMySqlTest {

    private Connection connection;

    @Before
    public void before() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        EmbeddedDbManager.getInstance().init("ut", "/sql/init/*.sql");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3310/ut", "root", "");
    }

    @Test
    public void testDb() throws Exception{
        //测试表正常初始化
        PreparedStatement ps = connection.prepareStatement("select * from t_user");
        ResultSet rs = ps.executeQuery();
        Assert.assertFalse(rs.next());

        //测试插入数据
        EmbeddedDbManager.getInstance().runScript("/sql/data/userData.sql");
        ps = connection.prepareStatement("select * from t_user where member_id = 101");
        rs = ps.executeQuery();
        String userName = "";
        int id = 0;
        while (rs.next()){
            id = rs.getInt("id");
            userName = rs.getString("user_name");
        }
        Assert.assertEquals(2, id);
        Assert.assertEquals("ct", userName);

        //测试清空数据库
        EmbeddedDbManager.getInstance().reload();
        ps = connection.prepareStatement("select * from t_user");
        rs = ps.executeQuery();
        Assert.assertFalse(rs.next());
    }


}
