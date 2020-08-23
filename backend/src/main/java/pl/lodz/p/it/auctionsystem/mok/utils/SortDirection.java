package pl.lodz.p.it.auctionsystem.mok.utils;

import org.springframework.data.domain.Sort.Direction;

public class SortDirection {
    
    public static Direction getSortDirection(String direction) {
        if (direction.equals("asc"))
            return Direction.ASC;
        else if (direction.equals("desc"))
            return Direction.DESC;
        
        return Direction.ASC;
    }
}