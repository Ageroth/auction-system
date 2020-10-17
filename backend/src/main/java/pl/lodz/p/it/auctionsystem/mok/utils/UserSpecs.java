package pl.lodz.p.it.auctionsystem.mok.utils;

import org.springframework.data.jpa.domain.Specification;
import pl.lodz.p.it.auctionsystem.entities.User;

/**
 * Klasa zawierająca metody wykorzystywane do zaawansowanych zapytań bazy danych.
 */
public class UserSpecs {
    
    /**
     * Służy do znalezienia encji {@link User} zawierających daną frazę w imieniu, nazwisku lub nazwie użytkownika.
     *
     * @param text poszukiwana fraza
     * @return obiekt typu {@link Specification}
     */
    public static Specification<User> containsTextInName(String text) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        String finalText = text;
        return (root, query, builder) -> builder.or(
                builder.like(root.get("firstName"), finalText),
                builder.like(root.get("lastName"), finalText),
                builder.like(root.get("username"), finalText)
        );
    }
    
    /**
     * Służy do znalezienia encji {@link User} w zależności od parametru aktywacji konta.
     *
     * @param status status aktywacji konta
     * @return obiekt typu {@link Specification}
     */
    public static Specification<User> isActive(Boolean status) {
        return (root, query, builder) -> builder.equal(root.get("activated"), status);
    }
}