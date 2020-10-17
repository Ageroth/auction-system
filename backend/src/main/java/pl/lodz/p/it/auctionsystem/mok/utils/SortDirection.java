package pl.lodz.p.it.auctionsystem.mok.utils;

import org.springframework.data.domain.Sort.Direction;

/**
 * Klasa, która reprezentuje opcję sortowania.
 */
public class SortDirection {
    
    /**
     * Zwraca kierunek sortowania na podstawie zadanego obiektu {@link String}.
     *
     * @param direction obiekt typu {@link String} reprezentujący kierunek sortowania
     * @return obiekt typu {@link Direction} reprezentujący kierunek sortowania
     */
    public static Direction getSortDirection(String direction) {
        if (direction.equals("asc"))
            return Direction.ASC;
        else if (direction.equals("desc"))
            return Direction.DESC;
        
        return Direction.ASC;
    }
}