package bnilive.bnishop.controller;

import bnilive.bnishop.domain.item.Book;
import bnilive.bnishop.domain.item.Item;
import bnilive.bnishop.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping(value = "/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping(value = "/items/new")
  public String create(BookForm form) {

    Book book = new Book();
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/items";
  }

  @GetMapping(value = "/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @PostMapping(value = "/items/{itemId}/edit")
  public String updateItem(@ModelAttribute("form") BookForm form) {
    itemService.updateItem(form.getId(), form.getName(), form.getPrice());
    return "redirect:/items";
  }
}