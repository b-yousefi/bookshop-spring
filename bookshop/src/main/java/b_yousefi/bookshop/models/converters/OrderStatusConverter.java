package b_yousefi.bookshop.models.converters;

import b_yousefi.bookshop.models.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by: b.yousefi
 * Date: 7/6/2020
 */
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return orderStatus.getStatusCode();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer stCode) {
        if (stCode == null) {
            return null;
        }

        return OrderStatus.of(stCode);
    }
}
