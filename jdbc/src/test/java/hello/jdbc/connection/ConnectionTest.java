package hello.jdbc.connection;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD); //커넥션 획득1
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD); //커넥션 획득2
        log.info("connection1={}, class={}",connection1,connection1.getClass());
        log.info("connection2={}, class={}",connection2,connection2.getClass());

        //DriverManager 커넥션을 획득할 때마다 URl,USERNAME,PASSWORD 같은 파라미터 전달

    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        // DriverMangerSource - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);

        //DataSource를 사용하는 방식은 처음 객체를 생성할때만 필요한 파라미터 남겨두고 커넥션을 획득할때는
        // dataSource.getConection()만 호출하면 된다.
        // 설정과 사용 분리 !

    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        log.info("connection1={}, class={}",connection1,connection1.getClass());
        log.info("connection2={}, class={}",connection2,connection2.getClass());

    }








}
