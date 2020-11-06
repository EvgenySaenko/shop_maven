package com.geekbrains.shop.paypal;


import com.geekbrains.shop.entities.Order;
import com.geekbrains.shop.services.OrdersService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/paypal")
public class PayPalController {
    private String clientId = "AQBPEMYGhUxgO3qAlU57hV-eL0kUqq5kdLs2uNoGKaw7m0fMUCMPwC7RoVj7ALSjv1COir5h2MT6NtER";
    private String clientSecret = "EPA9mRuJvwkx8XiNNDWjwzTXcNL3_lNBXpCILXgfNe9MUWhYfXElD96RujVipi08LZQ0EZ4Leiw3ExcO";
    private String mode = "sandbox";

    private APIContext apiContext = new APIContext(clientId, clientSecret, mode);

    private OrdersService ordersService;

    @Autowired
    public void setOrderService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping("/buy/{orderId}")
    public String buy(Model model, @PathVariable(name = "orderId") Long orderId, Principal principal) {
        try {
            Order order = ordersService.findById(orderId).get();

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8189/market/paypal/cancel");
            redirectUrls.setReturnUrl("http://localhost:8189/market/paypal/success/" + order.getId());

            Amount amount = new Amount();
            amount.setCurrency("RUB");
            amount.setTotal(order.getPrice().toString());

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Покупка в September Market");

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            Payment payment = new Payment();
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(transactions);
            payment.setIntent("sale");

            Payment doPayment = payment.create(apiContext);

            Iterator<Links> links = doPayment.getLinks().iterator();

            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("message", "Вы сюда не должны были попасть...");
        return "paypal-result";
    }

    @GetMapping("/success/{orderId}")
    public String success(HttpServletRequest request, HttpServletResponse response, Model model, @PathVariable(name = "orderId") Long orderId) {
        try {
            String paymentId = request.getParameter("paymentId");
            String payerId = request.getParameter("PayerID");

            if (paymentId == null || paymentId.isEmpty() || payerId == null || payerId.isEmpty()) {
                return "redirect:/";
            }

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            if (executedPayment.getState().equals("approved")) {
                model.addAttribute("message", "Ваш заказ #" + orderId + " сформирован и оплачен");
            } else {
                model.addAttribute("message", "Что-то пошло не так при формировании заказа, попробуйте повторить операцию");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "paypal-result";
    }

    @GetMapping("/cancel")
    public String cancel(Model model) {
        model.addAttribute("message", "Оплата заказа была отменена");

        return "paypal-result";
    }
}