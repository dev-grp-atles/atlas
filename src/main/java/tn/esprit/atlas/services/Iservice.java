package tn.esprit.atlas.services;
import java.util.List;

public interface Iservice <T>{

    public void addAirline(T t);
    public void updateAirline(T t);

    public void deleteAirline(T t);

    public List<T> getall();

    public T getone();

}
