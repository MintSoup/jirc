package am.aua.sas.jirc.persistence;

public interface AppendOnlyRepository<T> {
    T get(int index);

    boolean add(T element);
}
