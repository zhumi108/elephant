package com.tt.elephant.jwt;


public interface JwtConstant {
    /**
     * 签名秘钥
     */
  String SECRET = "tiny";

    /**
     * 过期时间  单位分钟
     */
    Integer EXPIRES_MINUTE = 60;
    /**
     * key token
     */
    String TOKEN = "token";

    /**
     * 登录名
     */
    String CLAIM_KEY_USER_ID="userId";
    /**
     * 用户名称（真实名称）
     */
  String CLAIM_KEY_USER_NAME="userName";
    /**
     * 用户类型：staff，client
     */
  String CLAIM_KEY_USER_TYPE="userType";
    /**
     * 员工所有拥有的权限列表
     */
     String CLAIM_KEY_PERMISSION_CODE_LIST="permissionCodeList";

}
