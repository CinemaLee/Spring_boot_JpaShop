package com.example.JpaShop.controller;

import com.example.JpaShop.Repository.OrderSearch;
import com.example.JpaShop.domain.Member;
import com.example.JpaShop.domain.Order;
import com.example.JpaShop.domain.item.Item;
import com.example.JpaShop.service.ItemService;
import com.example.JpaShop.service.MemberService;
import com.example.JpaShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){

        // ctrl + alt + v = 알맞은 변수와 타입으로 뽑아주기
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }


    @PostMapping("/order")   // Form에서 넘어온 name옵션들.
    public String order(@RequestParam Long memberId,
                        @RequestParam Long itemId,
                        @RequestParam int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }


    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch,Model model){
        List<Order> orders = orderService.findOrders(orderSearch);

        model.addAttribute("orders",orders);

        return "order/orderList";

    }


    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
