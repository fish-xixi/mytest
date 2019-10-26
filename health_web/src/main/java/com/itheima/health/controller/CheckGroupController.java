package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constants.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:56
 * @Version V1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;

    // 新增
    @RequestMapping(value = "/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer [] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    // 分頁查詢
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkGroupService.pageQuery(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    // ID查询
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        CheckGroup checkGroup = checkGroupService.findById(id);
        if(checkGroup!=null){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }
        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    // 使用检查组id，查询检查组对应检查项的集合，对应json的数据格式是：[28,29,30,31,32]
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
        List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return checkitemIds;
    }

    // 编辑检查组
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer [] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    // 查询所有检查组的集合
    @RequestMapping(value = "/findAll")
    public Result findAll(){
        List<CheckGroup> list = checkGroupService.findAll();
        if(list!=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }else{
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
