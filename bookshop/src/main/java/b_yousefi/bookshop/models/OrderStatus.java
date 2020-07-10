package b_yousefi.bookshop.models;

import java.util.stream.Stream;

/**
 * Created by: b.yousefi
 * Date: 7/6/2020
 */
public enum OrderStatus {
    OPEN(0),
    ORDERED(1),
    PAID(2),
    PROCESSED(3),
    PREPARED(4),
    DELIVERED(5),
    UNSUCCESSFUL(6),
    FUTURE(7);
    private int stCode;

    OrderStatus(int stCode) {
        this.stCode = stCode;
    }

    public static OrderStatus of(int stCode) {
        return Stream.of(OrderStatus.values())
                .filter(p -> p.getStatusCode() == stCode)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getStatusCode() {
        return stCode;
    }
}
