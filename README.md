# 基于 springboot + vue 的医院预约挂号系统

## 一、项目简介

* **语言：** java

* **框架 ：** springboot(2.5.9) + vue（2.7.14）

* **数据库：** mysql（8.0.35） + mybatis（2.2.1）

* **jdk ：** java21 *（springboot2.5.9对应的jdk版本需要java16以上）*。

* **其他工具：** pagehelper（前端分页），wangEditor（富文本框） 

   

## 二、运行项目需要进行的准备工作

* **代码编辑器：** idea2023.2专业版（社区版无法连接数据库）

* **maven** 

  * **maven** 配置不正确会导致包无法解析 

  * 这一步在准备时最容易出错 **setings** 中配置的 **jdk** 、**jdk** 、项目模块的 **jdk** 要对应，否则会报错（无效的发行版本）如下图

    ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 122417.png)



* **[从github仓库获取代码](https://github.com/PolarQAQ/hospital)**
  注：在 **github** 中的项目 **vue/src/views/manager** 文件夹下缺少一个 **User.vue** 文件，前端运行会报错



## 三、代码分析

* **common：** 其中包含的是拦截器及jwt令牌的设置与功能的编写（登录） 
  * **enums：** 包含的是项目中的枚举类 
  * **Result：** 设置前后端传输数据的结构(code, data, massage) 

* **controller:**

  * **springboot** 中的控制层，调用了 **service** 的方法和功能（**与前端交互**、**接受前端的数据**或者**向前端传输数据** )

  * **举例说明：** 

    ```java
    @RestController
    @RequestMapping("/admin")
    public class AdminController {
    
        @Resource
        private AdminService adminService;
    
        /**
         * 新增
         */
        @PostMapping("/add")
        public Result add(@RequestBody Admin admin) {
            adminService.add(admin);
            return Result.success();
        }
    ```

    这段代码是接收前端的一个 **admin** 对象，再调用 **service** 层的 **add** 方法将 **admin** 对象存入数据库中

* **entity:**

  * **entity** 中包含了项目中所有的实体类（医生，管理员，用户等 )

  * **举例说明**

    ```java
    public class Admin extends Account implements Serializable {
        private static final long serialVersionUID = 1L;
    
        /** ID */
        private Integer id;
        /** 用户名 */
        private String username;
        /** 密码 */
        private String password;
        /** 姓名 */
        private String name;
        /** 电话 */
        private String phone;
        /** 邮箱 */
        private String email;
        /** 头像 */
        private String avatar;
        /** 角色标识 */
        private String role;
    
        @Override
        public Integer getId() {
            return id;
        }
    
        @Override
        public void setId(Integer id) {
            this.id = id;
        }
    
        @Override
        public String getUsername() {
            return username;
        }
    
        @Override
        public void setUsername(String username) {
            this.username = username;
        }
    
        @Override
        public String getPassword() {
            return password;
        }
    
        @Override
        public void setPassword(String password) {
            this.password = password;
        }
    
        @Override
        public String getName() {
            return name;
        }
    
        @Override
        public void setName(String name) {
            this.name = name;
        }
    
        public String getPhone() {
            return phone;
        }
    
        public void setPhone(String phone) {
            this.phone = phone;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        @Override
        public String getAvatar() {
            return avatar;
        }
    
        @Override
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    
        @Override
        public String getRole() {
            return role;
        }
    
        @Override
        public void setRole(String role) {
            this.role = role;
        }
    }
    ```

    样例中的 **Admin** 类继承了 **Account** 的属性，同时添加了自己的属性

* **exception:**

  * 其中包含项目用来处理异常的类 

  * 样例

  * ```java
      private static final Log log = LogFactory.get();
  
  
        //统一异常处理@ExceptionHandler,主要用于Exception
        @ExceptionHandler(Exception.class)
        @ResponseBody//返回json串
        public Result error(HttpServletRequest request, Exception e){
            log.error("异常信息：",e);
            return Result.error();
        }
  
* **mapper：**

  * 持久层，项目通过 **mapper** 层和数据库进行交互（进行数据的增删改查），一个其中一个接口和一个 **xml** 文件*（二者需要同包同名才能够正确映射）*

  * 样例

  * ```java
    package com.example.mapper;
    
    import com.example.entity.Admin;
    import org.apache.ibatis.annotations.Select;
    
    import java.util.List;
    
    /**
     * 操作admin相关数据接口
    */
    public interface AdminMapper {
    
        /**
          * 新增
        */
        int insert(Admin admin);
    
        /**
          * 删除
        */
        int deleteById(Integer id);
    
        /**
          * 修改
        */
        int updateById(Admin admin);
    
        /**
          * 根据ID查询
        */
        Admin selectById(Integer id);
    
        /**
          * 查询所有
        */
        List<Admin> selectAll(Admin admin);
    
        @Select("select * from admin where username = #{username}")
        Admin selectByUsername(String username);
    }
    ```

  * 可以看出在持久层中大部分都是对数据库进行增删改查的操作

* **service:**

  * 调用 **mapper** ,对 **controller** 接收的数据和 **mapper** 提取的数据进行逻辑处理

  * **举例说明：**

  *  ```java
     public void add(@NotNull Admin admin) {
             /*
               验证身份
              */
             Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
             if (ObjectUtil.isNotNull(dbAdmin)) {
                 throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
             }
             if (ObjectUtil.isEmpty(admin.getPassword())) {
                 admin.setPassword(Constants.USER_DEFAULT_PASSWORD);
             }
             if (ObjectUtil.isEmpty(admin.getName())) {
                 admin.setName(admin.getUsername());
             }
             admin.setRole(RoleEnum.ADMIN.name());
             adminMapper.insert(admin);
         }
     ```

  * 样例中的代码的功能是添加一个新的用户，此时已经从前端接收到了一个对象 *（由于 **User** 和 **Doctor** 类都继承了 **Admin** ,所以可以用 **Admin** 类表示二者）* 需要验证身份来确定添加那种类型的用户（ **User** 或者 **User** ）

* **utils:**

  * 项目中要用到的工具类*（ **token生成** 、 **token解析** 等）*

  * 代码如下

  * ```java
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
    
    
    ```

  * 入样例所示，在次项目中通过用户角色和用户密码生成 **token** 来，前端的请求必须要在请求头中携带 **token** 来请访问，否则会被拦截器拦截

* **resource**

  * **resource** 中放了 **mybatis** 中的 **xml** 文件和项目的配置文件，项目中采用的大部分是通过 **xml** 文件来编写 **sql** 语句

  * 样例

  * ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="com.example.mapper.AdminMapper">
    
        <sql id="Base_Column_List">
            id,username,password,name,phone,email,avatar,role
        </sql>
    
        <select id="selectAll" resultType="com.example.entity.Admin">
            select
            <include refid="Base_Column_List" />
            from admin
            <where>
                <if test="id != null"> and id= #{id}</if>
                <if test="username != null"> and username like concat('%', #{username}, '%')</if>
                <if test="password != null"> and password= #{password}</if>
                <if test="name != null"> and name= #{name}</if>
                <if test="phone != null"> and phone= #{phone}</if>
                <if test="email != null"> and email= #{email}</if>
                <if test="avatar != null"> and avatar= #{avatar}</if>
                <if test="role != null"> and role= #{role}</if>
            </where>
        </select>
    
        <select id="selectById" resultType="com.example.entity.Admin">
            select
            <include refid="Base_Column_List" />
            from admin
            where id = #{id}
        </select>
    
        <delete id="deleteById">
            delete from admin
            where  id = #{id}
        </delete>
    
        <insert id="insert" parameterType="com.example.entity.Admin" useGeneratedKeys="true">
            insert into admin
    #             prefix:给sql拼接的前缀
    #             suffix:给sql拼接后缀
    #             prefixOverrides:去除sql前面的关键字或字符，该关键字或字符由prefixOverride指定
    #             suffixOverrides:去除sql后面的关键字或字符，该关键字或字符由suffixOverrides指定
            <trim prefix="(" suffix=")" suffixOverrides=",">
                    <if test="id != null">id,</if>
                    <if test="username != null">username,</if>
                    <if test="password != null">password,</if>
                    <if test="name != null">name,</if>
                    <if test="phone != null">phone,</if>
                    <if test="email != null">email,</if>
                    <if test="avatar != null">avatar,</if>
                    <if test="role != null">role,</if>
            </trim>
            <trim prefix="values (" suffix=")" suffixOverrides=",">
                    <if test="id != null">#{id},</if>
                    <if test="username != null">#{username},</if>
                    <if test="password != null">#{password},</if>
                    <if test="name != null">#{name},</if>
                    <if test="phone != null">#{phone},</if>
                    <if test="email != null">#{email},</if>
                    <if test="avatar != null">#{avatar},</if>
                    <if test="role != null">#{role},</if>
            </trim>
        </insert>
    
        <update id="updateById" parameterType="com.example.entity.Admin">
            update admin
            <set>
                <if test="username != null">
                    username = #{username},
                </if>
                <if test="password != null">
                    password = #{password},
                </if>
                <if test="name != null">
                    name = #{name},
                </if>
                <if test="phone != null">
                    phone = #{phone},
                </if>
                <if test="email != null">
                    email = #{email},
                </if>
                <if test="avatar != null">
                    avatar = #{avatar},
                </if>
                <if test="role != null">
                    role = #{role},
                </if>
            </set>
            where id = #{id} 
        </update>
    
    </mapper>
    ```

  * 可以看出，**AdminMapper** 文件中编写的主要是与 **Admin** 数据库交互用的 **sql** 语句

  * 在 **application.yml** 文件中主要是项目的一些配置

  * 代码如下

  * ```yml
    server:
      port: 9090
    
    # 数据库配置
    spring:
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        username: root   #你本地的数据库用户名
        password: root #你本地的数据库密码
        url: jdbc:mysql://localhost:3306/manager?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=GMT%2b8&allowPublicKeyRetrieval=true
      servlet:
        multipart:
          max-file-size: 100MB
          max-request-size: 100MB
    
    # 配置mybatis实体和xml映射
    mybatis:
      mapper-locations: classpath:mapper/*.xml
      type-aliases-package: com.example.entity
      configuration:
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        map-underscore-to-camel-case: true
    
    # 分页
    pagehelper:
      helper-dialect: mysql   #指出要连接的数据库类型
      reasonable: true  #实行合理化分页，当设置未true时
      support-methods-arguments: true
      params: count=countSql
      page-size-zero: false #默认为false，当设置为true时，pageSize=0或RowBounds.limit=0时机会查询出全部结果，相当于没有分页，但传回的数据仍是page类型
    #  params ：为了支持 startPage(Object params) 方法，增加了该参数来配置参数映射，用于从对象中根据属性名取值
    # 可以配置 pageNum,pageSize,count,pageSizeZero,reasonable ，不配置映射的用默认值
    # 默认值为pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero
    # autoRuntimeDialect ：默认值为 false 。设置为 true 时，允许在运行时根据多数据源自动识别对应方言的分页
    # closeConn ：默认值为 true 。当使用运行时动态数据源或没有设置 helperDialect 属性自动获取数据库类型时，会自动获取一个数据库连接，
    # 通过该属性来设置是否关闭获取的这个连接，默认 true 关闭，设置为false 后，不会关闭获取的连接，这个参数的设置要根据自己选择的数据源来决定。
    
    
    ip: localhost
    ```

  * 如样例所示，配置文件中写了 **运行端口** 、**数据库** 、**传输文件** 、**驼峰映射** 、**logo日志** 、**分页** ，等功能的配置

* **pom.xml** 

  * **pom.xml** 中主要是项目导入的一些依赖，以及依赖的版本，**maven** 项目中都会带一个 **pom.xml** 用来管理 **jar** 包和版本

  * 代码如下

  * ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.example</groupId>
        <artifactId>springboot</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.5.9</version>
        </parent>
    
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
            <java.version>21</java.version>
        </properties>
    
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
    
            <!--mysql-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
            </dependency>
    
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.2.1</version>
            </dependency>
    
            <dependency>
                <groupId>com.github.pagehelper</groupId>
                <artifactId>pagehelper-spring-boot-starter</artifactId>
                <version>1.4.6</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.mybatis</groupId>
                        <artifactId>mybatis</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
    
    
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>5.8.18</version>
            </dependency>
    
            <dependency>
                <groupId>com.auth0</groupId>
                <artifactId>java-jwt</artifactId>
                <version>4.3.0</version>
            </dependency>
            <dependency>
                <groupId>org.jetbrains</groupId>
                <artifactId>annotations</artifactId>
                <version>RELEASE</version>
                <scope>compile</scope>
            </dependency>
    
        </dependencies>
    
        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <configuration>
                        <fork>true</fork>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <configuration>
                        <source>14</source>
                        <target>14</target>
                    </configuration>
                </plugin>
            </plugins>
        </build>
        <repositories>
            <repository>
                <id>public</id>
                <name>aliyun nexus</name>
                <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
                <releases>
                    <enabled>true</enabled>
                </releases>
            </repository>
        </repositories>
    </project>
    ```

==时间原因，且前端大量代码是固定模版，在此不再介绍==

## 四、项目的创新点和难点

* **表的设计**

  * 需要设计哪些表 

  * 表中需要有哪些字段 

  * 尽可能用较少的表去完成较多的功能 

  * 尽可能不出现冗余的字段

  * 数据库中各个表中的关联关系以及外键的设置

  * **只用八张表完成了医生，病人，管理员不同身份的不同服务，包括但不限于**

    1. 病人注册功能的实现

    2. **病人查看医生排班功能的实现**

    3. 病人预约挂号功能的实现

    4. 病人查看系统公告的实现

    5. 病人充值功能的实现

    6. 病人住院功能的实现

    7. 医生叫号功能的实现

    8. 医生看病填写医嘱功能的实现

    9. 医生查看排班功能的实现

       ·······

* **模块功能**

  * **医生排班模块** 
    * 在医生排班的实现过程中需要与医生信息，科室信息，解析用户的角色，根据星期几去**实时动态地显示当天在值班的医生、该医生当天能够接诊的病人个数、以及该医生的挂号费用，同时对于不同身份的账户挂号给予不同的回应**  *（例如：医生和管理员用户不能挂号，却能够查看当天在班医生的信息，当这两种角色的用户挂号时会给与弹窗提示无法挂号。但当账号的角色是病人时则显示挂号成功等待所挂的医生叫号）* 
    * **病人通过医生排班信息挂号后，会根据医生的挂号费扣除余额*（如果余额不足则会提醒病人充值）*在病人所挂号的医生叫号之前病人可以在挂号页面取消挂号并返回余额**

  * 文件上传模块 
    * 需要反复解析 **token**
    * 解决在多个文件同时传输文件时可能会出现传输错误的情况
    * 医生填写医嘱界面对于 **wangEditor** 使用
    * **在医生填写医嘱病例时刻以图片，文档，视频，pdf，word 等形式上传**
  * **医生叫号模块** 
    * 在医生叫号时，选择病人是否住院，将数据同步到服务器，由管理员来对病人住院进行登记收费
  * 报错样例
    * ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 161526.png)
    * ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 161539.png)
    * ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 161547.png)
    * ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 161547.png)
    * ![](D:\OneDrive\Pictures\Screenshots\屏幕截图 2024-06-10 161553.png)

## 五 结束

# 感谢聆听，谢谢大家