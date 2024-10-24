package hello.jdbc.connection;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class DBConnectionUtilTest {

    @Test
    void connection(){
        Connection connection = DBConnectionUtil.getConnection();
        assertThat(connection).isNotNull();  // 로그 : url=jdbc:h2:tcp://localhost/~/test user=SA, class =class org.h2.jdbc.JdbcConnection
    }
}
