package com.tt.elephant.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;


public class JwtSupport {

    static final Logger LOGGER = LoggerFactory.getLogger(JwtSupport.class);

    /**
     * JWT 生成
     * @return
     */
    public static  String genereateToken(String userId,String userName){

        Calendar nowTime=Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, JwtConstant.EXPIRES_MINUTE);
        Date expires = nowTime.getTime();

        return JWT.create().withIssuedAt(new Date())
                .withExpiresAt(expires)
                .withClaim(JwtConstant.CLAIM_KEY_USER_ID,userId)
                .withClaim(JwtConstant.CLAIM_KEY_USER_NAME, userName)
                .sign(getAlgorithm());
    }


    public static String refreshToken(String token){
        String username = JWT.decode(token).getClaim(JwtConstant.CLAIM_KEY_USER_NAME).asString();
        String userId = JWT.decode(token).getClaim(JwtConstant.CLAIM_KEY_USER_ID).asString();
      return  genereateToken(userId,username);
    }

    public static Claim getClaimByToken(String token){
        return JWT.decode(token).getClaim(JwtConstant.CLAIM_KEY_USER_ID);
    }




    /**
     * 获取签名算法
     * @param secret
     * @return
     */
    public static Algorithm  getAlgorithm(String secret){
        return Algorithm.HMAC256(secret);
    }
    /**
     * 获取签名算法
     * @param
     * @return
     */
    public static Algorithm  getAlgorithm(){
        return Algorithm.HMAC256(JwtConstant.SECRET);
    }

    /**
     * token验证（是否为jwt格式）
     * @param token
     * @return
     */
   public static Boolean  verifyToken(String token){
        try {
            DecodedJWT verifier = JWT.require(getAlgorithm()).build().verify(token);
            return true;
        }catch (Exception exception) {
            return false;
        }
   }

    /**
     * 获得过期时间
     * @param token
     * @return
     */
    public static Date getExpireTime(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();

        } catch (JWTDecodeException e) {
            return null;
        }
    }


    public static String getUserName(String token){
        return getString(token, JwtConstant.CLAIM_KEY_USER_NAME );
    }

    public static String getUserId(String token){
        return getString(token, JwtConstant.CLAIM_KEY_USER_ID);
    }



    /**
     * 获得字符串
     * @param token
     * @param key
     * @return
     */
    public static String getString(String token,String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取Integer类型数据
     * @param token
     * @param key
     * @return
     */
    public static Integer getInteger(String token,String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asInt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得数组
     * @param token
     * @param key
     * @return
     */
    public static String[] getArray(String token, String key) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(key).asArray(String.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }



}
