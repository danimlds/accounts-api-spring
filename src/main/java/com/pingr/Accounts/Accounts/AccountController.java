package com.pingr.Accounts.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createOneAccount(@RequestBody Account account) {
        System.out.println(account);

        return this.service.createAccount(account);
    }

    @PutMapping("/{id}")
    public String updateAccount(@PathVariable("id") Long id, @RequestBody Account account) {
        Account acc = service.updateAccount(account, id);
        return "Account updated successfully.: " +acc.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.deleteAccount(id);
        return "Account successfully deleted.!";
    }

    @GetMapping
    public Iterable<Account> get() {
        return service.getAccounts();
    }

}
