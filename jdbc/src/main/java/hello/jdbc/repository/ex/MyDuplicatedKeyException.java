package hello.jdbc.repository.ex;

public class MyDuplicatedKeyException extends MyDbException{ // 데이터 중복의 경우에만 예외

    public MyDuplicatedKeyException() {
    }

    public MyDuplicatedKeyException(String message) {
        super(message);
    }

    public MyDuplicatedKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicatedKeyException(Throwable cause) {
        super(cause);
    }
}
