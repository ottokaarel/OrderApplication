package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "my_seq", sequenceName = "seq1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    public Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "order_number")
    public String orderNumber;


    @Valid
    @CollectionTable(
            name = "order_rows",
            joinColumns=@JoinColumn(name = "orders_id",
                    referencedColumnName = "id")
    )
    @ElementCollection(fetch = FetchType.EAGER)
    public List<OrderRow> orderRows = new ArrayList<>();



}
