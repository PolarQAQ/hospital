package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.CallEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Doctor;
import com.example.entity.Reserve;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.DoctorMapper;
import com.example.mapper.ReserveMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ReserveService {

    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private DoctorMapper doctorMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     * 注意开启事务
     */
    @Transactional
    public void add(Reserve reserve) {
        reserve.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        reserve.setStatus(CallEnum.STATUS_NO.status);
        List<Reserve> reserves = reserveMapper.selectAll(reserve);
        if (CollectionUtil.isNotEmpty(reserves)) {
            throw new CustomException("-1", "您已挂号成功，请勿重复挂号，耐心等待即可");
        }
        //获取当前医生的挂号价格
        User user = userMapper.selectById(reserve.getUserId());
        Doctor doctor = doctorMapper.selectById(reserve.getDoctorId());
        if(user.getAccount() < doctor.getPrice()) {
            throw new CustomException("-1", "您的余额不足，请充值");
        }
        reserve.setStatus(CallEnum.STATUS_NO.status);
        reserveMapper.insert(reserve);
        user.setAccount(user.getAccount() - doctor.getPrice());
        userMapper.updateById(user);
    }

    /**
     * 删除
     */
    @Transactional
    public void deleteById(Integer id) {

        Reserve reserve = reserveMapper.selectById(id);
        Doctor doctor = doctorMapper.selectById(reserve.getDoctorId());
        User user = userMapper.selectById(reserve.getUserId());

        /**
         * 如果未叫号取消挂号则将费用返还
         */
        if(reserve.getStatus().equals(CallEnum.STATUS_NO.status)){
            user.setAccount(user.getAccount() + doctor.getPrice());
            userMapper.updateById(user);
        }
        reserveMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    @Transactional
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Reserve reserve) {
        reserveMapper.updateById(reserve);
    }

    /**
     * 根据ID查询
     */
    public Reserve selectById(Integer id) {
        return reserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reserve> selectAll(Reserve reserve) {
        return reserveMapper.selectAll(reserve);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reserve> selectPage(Reserve reserve, Integer pageNum, Integer pageSize) {
        /*
        通过解析token获取当前的角色，从而进行不同的操作
         */
        Account currentUser = TokenUtils.getCurrentUser();
        /**
         * 如果是用户则通过用户的id查询，下面以此类推
         */
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            reserve.setUserId(currentUser.getId());
        }
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())) {
            reserve.setDoctorId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> list = reserveMapper.selectAll(reserve);
        return PageInfo.of(list);
    }

}