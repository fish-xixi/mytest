package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSpringSecurity {
    // SpringSecurity加盐加密
    @Test
    public void testSpringSecurity(){
        // $2a$10$dyIf5fOjCRZs/pYXiBYy8uOiTa1z7I.mpqWlK5B/0icpAKijKCgxe
        // $2a$10$OphM.agzJ55McriN2BzCFeoLZh/z8uL.8dcGx.VCnjLq01vav7qEm

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String s = encoder.encode("123");
        System.out.println(s);
        String s1 = encoder.encode("123");
        System.out.println(s1);

        // 进行判断
        boolean b = encoder.matches("1234", "$2a$10$MId7m76tuRXufLeeNagt1.NDOtT1GhZTeR4RwS7z7C6vp0LCcRqVa");
        System.out.println(b);
    }
}