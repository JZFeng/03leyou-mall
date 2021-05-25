/**
 * @Author jzfeng
 * @Date 5/24/21-3:55 PM
 */

package com.leyou.user.controller;


import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserController {

    @Autowired
    private UserService userService;
}
