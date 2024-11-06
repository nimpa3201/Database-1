package hello.jdbc.connection;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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

    }



}
