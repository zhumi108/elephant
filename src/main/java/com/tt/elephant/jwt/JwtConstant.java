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
     * 用户唯一ID
     */
    String CLAIM_KEY_USER_ID="userId";
    /**
     * 用户邮箱地址
     */
  String CLAIM_KEY_EMAIL_ADDRESS="emailAddress";
  /**
   * 用户昵称
   */
  String claim_KEY_NICKNAME="nickname";
    /**
     * 用户类型：staff，client
     */
  String CLAIM_KEY_USER_TYPE="userType";
    /**
     * 员工所有拥有的权限列表
     */
     String CLAIM_KEY_PERMISSION_CODE_LIST="permissionCodeList";

}
