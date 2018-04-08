package cn.e3mall.sso.service.impl;

import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    /*过期时间*/
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Autowired
    private JedisClientCluster jedisClientCluster;

    @Override
    public E3Result getUserByToken(String token) {
        String json=jedisClientCluster.get("SESSION:"+token);
        //判断
        if (!StringUtils.isNotBlank(json))
        {
            return E3Result.build(201,"用户登录已过期");
        }
        //去用户信息  更新时间
        jedisClientCluster.expire("SESSION:"+token,SESSION_EXPIRE);
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
        return E3Result.ok(tbUser);
    }
}
