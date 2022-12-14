package th.co.readypaper.billary.common.model;

import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface LineItemMapper<T extends Persistable<UUID>> {

    T update(@MappingTarget T target, T source);

    @Named("lineItems")
    default void update(@MappingTarget List<T> target, List<T> source) {
        List<T> items = new ArrayList<>();
        if (target.size() == source.size()) {
            for (int i = 0; i < target.size(); i++) {
                if (target.get(i).getId().equals(source.get(i).getId())) {
                    items.add(update(target.get(i), source.get(i)));
                }
            }
        } else if (target.size() < source.size()) {
            for (int i = 0; i < source.size(); i++) {
                if (i < target.size()) {
                    if (target.get(i).getId().equals(source.get(i).getId())) {
                        items.add(update(target.get(i), source.get(i)));
                    }
                } else {
                    items.add(source.get(i));
                }
            }
        } else {
            for (int i = 0; i < target.size(); i++) {
                if (i < source.size()) {
                    if (target.get(i).getId().equals(source.get(i).getId())) {
                        items.add(update(target.get(i), source.get(i)));
                    }
                }
            }
        }

        target.clear();
        target.addAll(items);
    }
}
