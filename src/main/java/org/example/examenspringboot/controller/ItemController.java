package org.example.examenspringboot.controller;

import org.example.examenspringboot.exception.ItemNotFoundException;
import org.example.examenspringboot.model.Item;
import org.example.examenspringboot.persistency.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
class ItemController {
    private final ItemRepository itemRepository;
    ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // TODOS LOS ITEMS
    @GetMapping("/all_items")
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
    // ADD ITEM
    @PostMapping("/add_item")
    public Item save(@RequestBody Item equipo) {
        return itemRepository.save(equipo);
    }
    // DELETE ITEM POR ID
    @DeleteMapping("/borrar_item_id/{id}")
    public void delete(@PathVariable String id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            throw new ItemNotFoundException("No hay ningún item con el id: "+id);
        }
        itemRepository.delete(item);
    }
    // DELETE ITEM POR NOMBRE
    @DeleteMapping("/borrar_item_nombre")
    public void deleteByName(@RequestParam String nombre) {
        Item item = itemRepository.findFirstByTitle(nombre);
        if (item == null) {
            throw new ItemNotFoundException("No hay ningún item con el nombre: "+nombre);
        }
        itemRepository.delete(item);
    }
    // GET ITEM ID
    @GetMapping("/item_id/{id}")
    public Item findById(@PathVariable String id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("No hay ningún item con el id: "+id);
        }
    }
    // GET ITEM POR NOMBRE
    @GetMapping("/item_nombre")
    public Item findByNombre(@RequestParam String nombre) {
        Item item = itemRepository.findFirstByTitle(nombre);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("No hay ningún item con el nombre: "+nombre);
        }
    }
    // GET ALL ITEMS DE CATEGORIA
    @GetMapping("/item_categoria")
    public List<Item> findByCategoria(@RequestParam String categoria) {
        List<Item> item = itemRepository.findItemsByCategory(categoria);
        if (item != null) {
            return item;
        } else {
            throw new ItemNotFoundException("No hay ningún item con la categoría: "+categoria);
        }
    }
    // UPDATE ITEM CATEGORIA
    @PutMapping("/actualizar_item_id/{id}")
    public Item update(@PathVariable String id, @RequestBody Item item) {
        Item itemExistente = itemRepository.findById(id).orElse(null);
        if (itemExistente != null) {
            itemExistente.setCategory(item.getCategory());
            return itemRepository.save(itemExistente);
        } else {
            throw new ItemNotFoundException("No hay ningún item con el id: "+id);
        }
    }
    // GET ESTADÍSTICAS
    @GetMapping("/estadisticas")
    public ResponseEntity<String> getEstadisticas() {
        List<Item> lista = itemRepository.findAll();

        long numItems = itemRepository.count();

        int constante = 100;

        List<Item> menorQueConstante = itemRepository.findItemsByCountLessThan(constante);
        List<String> nombresMenorQueConstante = new ArrayList<>();

        for (Item item: menorQueConstante) {
            nombresMenorQueConstante.add(item.getTitle());
        }


        List<String> fabricantes = new ArrayList<>();
        for (Item item : lista) {
            if (!fabricantes.contains(item.getManufacturer())) {
                fabricantes.add(item.getManufacturer());
            }
        }

        return ResponseEntity.ok("Número de items: " + numItems + "\n" + "Items con un stock menor a 100: " + nombresMenorQueConstante + "\n" + "Lista de fabricantes: " + fabricantes.toString());
    }


}

