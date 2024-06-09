package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * Token工具类
 */
@Component
public class TokenUtils {

    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Resource
    UserService userService;
    @Resource
    AdminService adminService;
    @Resource
    DoctorService doctorService;
    private static DoctorService staticDoctorService;
    private static UserService staticUserService;
    private static AdminService staticAdminService;

    /*
    PostConstruct 注释用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化。
     */
    @PostConstruct
    public void setUserService() {
        staticAdminService = adminService;
        staticDoctorService = doctorService;
        staticUserService = userService;
    }

    /**
     * 生成token
     * 通常token包含以下内容
     * header {
     *     "alg":"HMAC256",//加密算法
     *     "typ":"JWT",//token类型
     * }
     *
     * JWT的第二部分是payload，它包含声明（要求）。声明是关于实体(通常是用户信息，也就是程序员放入token中的数据)和其他数据的声明。声明有三种类型: registered, public 和 private。
     * Registered claims : 这里有一组预定义的声明，它们不是强制的，但是推荐。比如：iss (issuer), exp (expiration time), sub (subject), aud (audience)等。
     * Public claims : 可以随意定义。
     * Private claims : 用于在同意使用它们的各方之间共享信息，并且不是注册的或公开的声明。
     *
     * Payload {
     *    1-DOCTOR
     * }
     *
     * Signature:自己定义的内容
     * sign {
     *
     * }
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // 将 userId-role 保存到 token 里面,作为载荷中的audience
                /**
                 * 偏移时间
                 */
                .withExpiresAt(DateUtil.offsetHour(new Date(), 24)) // 24小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {

        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String token = request.getHeader(Constants.TOKEN);

//            log.info("解析的token" + token);

            if (ObjectUtil.isNotEmpty(token)) {

                /**
                 * token中的data包含id和role的内容
                 */
                String userRole = JWT.decode(token).getAudience().get(0);
                log.info("从token中获取信息" + userRole);
                String userId = userRole.split("-")[0];  // 获取用户id
                String role = userRole.split("-")[1];    // 获取角色

                /**
                 * 返回一个Account对象，对象中含有role属性，从而获取当前的用户的身份
                 */
                if (RoleEnum.ADMIN.name().equals(role)) {
                    return staticAdminService.selectById(Integer.valueOf(userId));
                }
                if(RoleEnum.DOCTOR.name().equals(role)) {
                   return staticDoctorService.selectById(Integer.valueOf(userId));
                }
                if(RoleEnum.USER.name().equals(role)) {
                    return staticUserService.selectById(Integer.valueOf(userId));
                }
            }
        } catch (Exception e) {
            log.error("获取当前用户信息出错", e);
        }
        return new Account();  // 返回空的账号对象
    }
}

