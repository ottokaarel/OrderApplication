package model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Table(name = "order_rows")
public class OrderRow {

    @NotNull
    @Column(name = "item_name")
    public String itemName;

    @NotNull
    @DecimalMin("1")
    public Integer quantity;

    @NotNull
    @DecimalMin("1")
    public Integer price;
}
