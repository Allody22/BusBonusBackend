package ru.nsu.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.model.user.Account;
import ru.nsu.repository.user.AccountRepository;

import javax.persistence.EntityNotFoundException;

@Service
@EnableTransactionManagement
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getAccountByPhone(username)
                .orElseThrow(() -> new EntityNotFoundException("Информация о пользователи не найдена с логином: " + username));

        return UserDetailsImpl.build(account);
    }
}
