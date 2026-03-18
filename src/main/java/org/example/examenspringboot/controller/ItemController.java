package org.example.examenspringboot.controller;

import org.example.examenspringboot.exception.ItemNotFoundException;
import org.example.examenspringboot.model.Item;
import org.example.examenspringboot.persistency.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ItemController {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Listar todos los items
    @GetMapping("/items")
    public String findAll(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items";
    }

    // Ver detalle de un item
    @GetMapping("/item/{id}")
    public String itemDetail(@PathVariable String id, Model model) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("No hay ningún item con el id: " + id));
        model.addAttribute("item", item);
        return "item";
    }

    // Mostrar formulario para editar un item
    @GetMapping("/edit/{id}")
    public String showEditItemForm(@PathVariable String id, Model model) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("No hay ningún item con el id: " + id));
        model.addAttribute("item", item);
        model.addAttribute("pageTitle", "Editar Item");
        model.addAttribute("action", "/items/update/" + id);
        return "item-form";
    }

    // Actualizar un item existente
    @PostMapping("/update/{id}")
    public String updateItem(@PathVariable String id, @ModelAttribute("item") Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("No hay ningún item con el id: " + id));

        item.setTitle(itemDetails.getTitle());
        item.setDescription(itemDetails.getDescription());
        item.setCategory(itemDetails.getCategory());
        item.setManufacturer(itemDetails.getManufacturer());
        item.setCount(itemDetails.getCount());
        itemRepository.save(item);
        return "redirect:/items";
    }

    // Borrar un item
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable String id) {
        itemRepository.findById(id).ifPresent(itemRepository::delete);
        return "redirect:/items";
    }

    // Mostrar estadísticas
    @GetMapping("/estadisticas")
    public String getEstadisticas(Model model) {
        long numItems = itemRepository.count();
        int constante = 100;
        List<Item> menorQueConstante = itemRepository.findItemsByCountLessThan(constante);

        List<String> fabricantes = itemRepository.findAll().stream()
                .map(Item::getManufacturer)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("numItems", numItems);
        model.addAttribute("itemsBajoStock", menorQueConstante);
        model.addAttribute("fabricantes", fabricantes);

        return "estadisticas";
    }
}
