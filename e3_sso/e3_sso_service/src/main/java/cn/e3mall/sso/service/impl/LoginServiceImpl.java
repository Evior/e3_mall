package cn.e3mall.sso.service.impl;

import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;

    /*redis*/
    @Autowired
    private JedisClientCluster jedisClientCluster;

    /*过期时间*/
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result userlogin(String username, String password) {
        //判断用户名密码是否正确
        TbUserExample tbUserExample=new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if (tbUsers==null||tbUsers.size()==0)
        {
            return E3Result.build(400,"用户名或密码错误");
        }
        TbUser user = tbUsers.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword()))
        {
            return E3Result.build(400,"用户名或密码错误");
        }
        //密码正确登录成功
        //生成token
        String token = UUID.randomUUID().toString();
        //把用户信息放入redis
        jedisClientCluster.set("SESSION:"+token, JsonUtils.objectToJson(user));
        //设置过期时间
        jedisClientCluster.expire("SESSION:"+token,SESSION_EXPIRE);
        return E3Result.ok(token);
    }
}
