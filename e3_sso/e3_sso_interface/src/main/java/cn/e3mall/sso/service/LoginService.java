package cn.e3mall.sso.service;

import cn.e3mall.comment.utils.E3Result;

public interface LoginService {
    public E3Result userlogin(String username,String password);
}
