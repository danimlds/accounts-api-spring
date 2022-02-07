package com.pingr.Accounts.Accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository repo;
    private final ProducerService producer;

    @Autowired
    public AccountService(AccountRepository repo, ProducerService producer) {
        this.repo = repo;
        this.producer = producer;
    }

    //   tem ID, está salva no banco
    //     |
    //     |         não tem ID, não está no banco ainda
    //     |                                  |
    //     v                                  v
    public Account createAccount(Account account) throws IllegalArgumentException {
        try {
            Account acc = this.repo.save(account);
            this.producer.emitAccountCreatedEvent(acc);
            return acc;
        } catch(Exception e) {
            throw new IllegalArgumentException("Account creation violates restrictions" + "[account: " + account + "]");
        }
    }

    public Optional<Account> getAccountById(Long id) {
        return repo.findById(id);
    }

    public Account updateAccount(Account account, Long id) {
        Assert.notNull(id, "Unable to update account!");
        Optional<Account> optional = getAccountById(id);
        if(optional.isPresent()) {
            Account db = optional.get();
            db.setUsername(account.getUsername());
            db.setEmail(account.getEmail());
            db.setPassword(account.getPassword());
            System.out.println("Account id: " +db.getId());
            repo.save(db);
            return db;
        }else {
            throw  new RuntimeException("Unable to update account!");
        }
    }

    public void deleteAccount(Long id) {
        Optional<Account> account = getAccountById(id);
        if(account.isPresent()) {
            repo.deleteById(id);
        }else {
            throw new RuntimeException("Unable to delete account!");
        }
    }

    public Iterable<Account> getAccounts() {
        return repo.findAll();
    }

}
