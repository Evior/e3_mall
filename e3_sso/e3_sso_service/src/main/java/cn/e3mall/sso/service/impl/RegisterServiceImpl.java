package cn.e3mall.sso.service.impl;

import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 根据参数和数据类型  判断用户输入的用户名、手机号、邮箱是否已经存在
     * @param param
     * @param type
     * @return
     */


    @Override
    public E3Result checkData(String param, Integer type) {
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if (type==1)
        {
           criteria.andUsernameEqualTo(param);
        }else if(type==2)
        {
            criteria.andPhoneEqualTo(param);
        }else if(type==3)
        {
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(400,"数据类型错误");
        }

        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if (tbUsers!=null&&tbUsers.size()>0)
        {
            return E3Result.build(400,"NO");
        }
        return E3Result.build(200,"OK");
    }

    @Override
    public E3Result register(TbUser user) {


        //数据有效性验证
        if (!StringUtils.isNotBlank(user.getUsername())&&!StringUtils.isNotBlank(user.getPhone())&&!StringUtils.isNotBlank(user.getPhone()))
        {
            return E3Result.build(400,"用户数据不完整,注册失败");
        }
        E3Result result = checkData(user.getUsername(), 1);
        if (!result.getMsg().equals("OK"))
        {
            return E3Result.build(400,"此用户已经被占用");
        }
        result = checkData(user.getPhone(), 2);
        if (!result.getMsg().equals("OK"))
        {
            return E3Result.build(400,"此号码已经被占用");
        }



        //补全
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //MD5加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        //用户插入数据库
        tbUserMapper.insert(user);
        return E3Result.ok();
    }
}
