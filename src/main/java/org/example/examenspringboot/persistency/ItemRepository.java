package org.example.examenspringboot.persistency;

import org.example.examenspringboot.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends MongoRepository<Item,String> {
    //    void deleteRutaByPropertiesNombre(String nombre);
    Item findFirstByTitle(String title);
    List<Item> findItemsByCategory(String categoria);
    List<Item> findItemsByCountLessThan(int constante);


}
