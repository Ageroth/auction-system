package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO wykorzystywane przy przesyłaniu parametrów do sortowania, filtrowania i stronnicowania.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCriteria {

    private int page = 0;

    private String sortField;

    private String order;

    private String query;

    private Boolean status;
}
