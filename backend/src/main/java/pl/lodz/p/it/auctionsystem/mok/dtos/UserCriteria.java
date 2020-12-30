package pl.lodz.p.it.auctionsystem.mok.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO zawierające parametry, wykorzystywane przy przesyłaniu parametrów do sortowania, filtrowania i stronnicowania.
 */
@Getter
@Setter
public class UserCriteria {

    private int page = 0;

    private String sortField;

    private String order;

    private String query;

    private Boolean status;
}