package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:54
 * @Version V1.0
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        // 方案一：如果不使用mybatis的分页插件
        // 总记录数：（sql：select count(*) from t_checkitem where name=#{queryString}）
        // 当前页的查询集合List：（sql：select * from t_checkitem where name=#{queryString} limit ?,?）
        // 第一个?表示当前页从第几条开始检索（起始记录数）：计算(currentPage-1)*pageSize
        // 第二个?表示当前页最多显示的记录数：计算pageSize
        // 方案二：使用mybatis的分页插件
        // 1：初始化参数
        PageHelper.startPage(currentPage,pageSize);
        // 2：查询数据
        List<CheckItem> list = checkItemDao.findPage(queryString);
        // 3：封装PageHelp对应的结果集
        PageInfo<CheckItem> pageInfo = new PageInfo<>(list);
        // 封装数据
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        // 或者使用
//        Page<CheckItem> page = checkItemDao.findPage(queryString);
//        return new PageResult(page.getTotal(),page.getResult());

    }

    @Override
    public void delete(Integer id) {
        // 在删除之前，判断检查组和检查项中间表中是否存在数据，有数据不能删除，没有数据可以删除
        long count = checkItemDao.findCheckGroupAndCheckItemByCheckItemId(id);
        // 此时存在数据，不能删除，提示用户
        if(count>0){
            throw new RuntimeException("中间表和检查组中存在关联数据，不能删除");
        }
        // 删除检查项
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
