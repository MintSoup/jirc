package am.aua.sas.jirc.persistence;

import java.util.Collection;

public interface Repository<T> {
    T get(int index);

    Collection<T> getAll();

    void add(T element);

    void set(int index, T element);

    void remove(int index);
}
