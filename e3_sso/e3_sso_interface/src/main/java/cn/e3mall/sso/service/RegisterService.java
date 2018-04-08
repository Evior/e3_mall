package cn.e3mall.sso.service;

import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

    public E3Result checkData(String param,Integer type);

    public E3Result register(TbUser user);


}
