package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("user")
public interface UserApi {

    @GetMapping("query")
    public User queryByNameAndPassword(@RequestParam("username")  String username, @RequestParam("password") String password);
}