package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

/**
 * @ClassName CheckItemDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/13 9:53
 * @Version V1.0
 */
public interface MemberDao {

    Member findMemberByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByRegTime(String regTime);

    Integer findTodayNewMember(String date);

    Integer findTotalMember();

    Integer findRegTimeAfterDateNewMember(String date);

}
