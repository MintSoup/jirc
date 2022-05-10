package am.aua.sas.jirc.persistence;

public interface Repository<T> {
    T get(int index);

    void add(T element);

    void set(int index, T element);

    void remove(int index);
}
