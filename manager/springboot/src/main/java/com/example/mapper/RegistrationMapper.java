package com.example.mapper;

import com.example.entity.Registration;

import java.util.List;

public interface RegistrationMapper {

    /**
     * 新增
     */
    int insert(Registration registration);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Registration registration);

    /**
     * 根据ID查询
     */
    Registration selectById(Integer id);

    /**
     * 查询所有
     */
    List<Registration> selectAll(Registration registration);
}