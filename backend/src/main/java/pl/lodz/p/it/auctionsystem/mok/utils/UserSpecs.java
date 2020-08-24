package pl.lodz.p.it.auctionsystem.mok.utils;

import org.springframework.data.jpa.domain.Specification;
import pl.lodz.p.it.auctionsystem.entities.User;

public class UserSpecs {
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
    
    public static Specification<User> isActive(Boolean status) {
        return (root, query, builder) -> builder.equal(root.get("activated"), status);
    }
}