package com.example.smartslate.repository;

import com.example.smartslate.model.User;

public interface ILoginRepository {

    User findByUsernameAndPassword(String usernameOrEmail, String password);
}
