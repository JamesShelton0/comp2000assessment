import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// adding generics yippee (to store only vehicles safely)

// type-safe collection for movable simulation entities
// bound means T must be Vehicle or one of Vehicle's subclasses

public class EntityStore<T extends Vehicle> {
    private final List<T> entities = new ArrayList<>();

    public void add(T entity) {
        if (entity == null) {
            // fail at point of mistake instead of when Panel is trying to draw
            throw new IllegalArgumentException("A simulation entity cannot be null.");
        }
        entities.add(entity);
    }

    public void remove(T entity) {
        if (entity == null) {
            // fail at point of mistake instead of when Panel is trying to draw
            throw new IllegalArgumentException("A simulation entity cannot be null.");
        }
        entities.remove(entity);
    }

    public Vehicle get(int index) {
        return entities.get(index);
    }

    public List<T> getEntities() {
        // read-only view, callers can iterate over vehicles but cant alter this store directly. vehicles themselves can still be changed e.g calling move()
        return Collections.unmodifiableList(entities);
    }

    public List<T> modifyEntities() {
        return entities;
    }
}
