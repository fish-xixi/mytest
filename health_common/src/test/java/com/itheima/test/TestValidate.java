package com.itheima.test;

import com.itheima.health.utils.ValidateCodeUtils;
import org.junit.Test;

/**
 * @ClassName TestValidate
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/10/19 9:08
 * @Version V1.0
 */
public class TestValidate {

    @Test
    public void testCode(){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(code);
        Integer code6 = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(code6);
    }
}
