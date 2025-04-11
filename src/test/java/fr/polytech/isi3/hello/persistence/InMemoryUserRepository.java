package fr.polytech.isi3.hello.persistence;

public class InMemoryUserRepository extends MapBasedUserRepository   {

    @Override
    protected void commit() {
        // Do nothing, data should not be persisted
    }
}
