package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:54
 * @Version V1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    // 会员
    @Autowired
    MemberDao memberDao;


    @Override
    public Member findMemberByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        // 对密码进行md5加密保存
        if(member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByRegTime(List<String> monthsList) {
        // 组织查询结果
        List<Integer> memberCountList = new ArrayList<>();
        for (String month : monthsList) {
            String regTime = month+"-31"; // 已知当前月，查询当前月的最后1天
            Integer memberCount = memberDao.findMemberCountByRegTime(regTime);
            memberCountList.add(memberCount);
        }
        return memberCountList;
    }
}
