package system2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**���ӵ�SQL server
 */
public class GetConnection {
    private Connection con=null;
    public Connection GetConnection(){
        String URL="jdbc:sqlserver://localhost:1433;DatabaseName=es";
        String USER="sa";
        String KEY="2543127wyq";

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con= DriverManager.getConnection(URL, USER, KEY);
            System.out.println("���ӳɹ�");
        } catch (Exception e) {
        	System.out.println("����ʧ��");
            e.printStackTrace();
        }
        return con;
    }
}
