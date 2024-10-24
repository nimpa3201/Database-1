package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection(){
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // JDBC에서 제공된 메서드, URL과 자격 증명을 기반으로 적절한 DB 드라이버의 구현체를 찾아 연결 생성
            log.info("get connection={}, class ={}",connection,connection.getClass());
            return connection;
        } catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }
 }
