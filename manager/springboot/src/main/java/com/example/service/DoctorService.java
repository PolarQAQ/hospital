package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Doctor;
import com.example.exception.CustomException;
import com.example.mapper.DoctorMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;

    public Account login(Account account) {
        Account dbDoctor = doctorMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbDoctor.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbDoctor.getId() + "-" + RoleEnum.DOCTOR.name();
        String token = TokenUtils.createToken(tokenData, dbDoctor.getPassword());
        dbDoctor.setToken(token);
        return dbDoctor;
    }

    /**
     * 新增
     */
    public void add(Doctor doctor) {
        Doctor dbDoctor = doctorMapper.selectByUsername(doctor.getUsername());
        if (ObjectUtil.isNotNull(dbDoctor)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(doctor.getPassword())) {
            doctor.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(doctor.getName())) {
            doctor.setName(doctor.getUsername());
        }
        doctor.setRole(RoleEnum.DOCTOR.name());
        doctorMapper.insert(doctor);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        doctorMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            doctorMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Doctor doctor) {
        doctorMapper.updateById(doctor);
    }

    /**
     * 根据ID查询
     */
    public Doctor selectById(Integer id) {
        return doctorMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Doctor> selectAll(Doctor doctor) {
        return doctorMapper.selectAll(doctor);
    }

    /**
     * 分页查询
     */
    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> list = doctorMapper.selectAll(doctor);
        return PageInfo.of(list);
    }
}