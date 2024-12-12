package hello.jdbc.repository;


import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/*
 * sqlExceptionTranslator 추가
 */

@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(DataSource dataSource) {

        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }



    @Override
    public Member save(Member member) {

        String sql = "insert into member(member_id,money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection(); //커넥션 획득
            pstmt = con.prepareStatement(sql); //SQL 전달
            pstmt.setString(1, member.getMemberId());  //바라미터 바인딩
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); 
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("save",sql,e);
         } finally {
            close(con, pstmt, null); // 리소스 정리
        }
    }


    @Override
    public Member findById(String memberId)  {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }


        } catch (SQLException e) {
            log.error("db error", e);
            throw exTranslator.translate("findById",sql,e);
        } finally {
            close(con, pstmt, rs);
        }


    }



    @Override
    public void update(String memberId, int money)  {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize ={}",resultSize);


        } catch (SQLException e) {
            throw exTranslator.translate("update",sql,e);
        } finally {
            close(con, pstmt, null); // 리소스 정리
        }
    }




    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();



        } catch (SQLException e) {
            throw exTranslator.translate("delete",sql,e);
        } finally {
            close(con, pstmt, null); // 리소스 정리
        }


    }



    private void close(Connection con, Statement stmt, ResultSet rs) {


        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        DataSourceUtils.releaseConnection(con,dataSource);
        /**
         * 트랜잭션이 활성화된 경우)
         *  트랜잭션 동기화 매니저(TransactionSynchronizationManager)가 현재 트랜잭션 컨텍스트와 연결된 커넥션을 관리.
         *  이 경우, releaseConnection()은 커넥션을 닫지 않고 트랜잭션이 끝날 때까지 유지.
         * 트랜잭션이 활성화 x )
         * 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우, 커넥션을 닫음
         *
         */


        //JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        // 주의 ! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다
        Connection con = DataSourceUtils.getConnection(dataSource); // 트랜잭션 동기화 매니저가 관리하는 커넥션이 있으면 해당 커넥션을 반환
        // 만약 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 새로운 커넥션을 생성해서 반환
        log.info("get connection = {}, class = {}", con , con.getClass());
        return con;
    }
}
