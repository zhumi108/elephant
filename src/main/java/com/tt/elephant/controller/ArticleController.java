package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.ArticleDto;
import com.tt.elephant.model.ArticleResponseInfo;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.repository.ArticleEntity;
import com.tt.elephant.repository.ArticleRepository;
import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.repository.UserRepository;
import com.tt.elephant.util.ServiceSupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "文章管理接口", description = "文章管理")
@RequestMapping("/api/v1")
@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleResposity;

    @Autowired
    UserRepository userRepository;

    /**
     * 获取文章列表
     * @param body
     * @return
     */
    @JwtToken
    @PostMapping("/article/list")
    public @ResponseBody ArticleResponseInfo ArticleList(@RequestBody Map body){
        int type = (int) body.get("type");
        int pageNumber = (int) body.get("pageNumber");
        int pageSize = (int) body.get("pageSize");
        String userId = ServiceSupport.getCurrentUserId();
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        ArticleResponseInfo responseInfo = new ArticleResponseInfo();
        Page<ArticleEntity> currentEntityList;

        if (type == 0) {
            // all user's articles
            currentEntityList = articleResposity.findByAllUsers(pageable);
        } else {
            // current user's articles
            currentEntityList = articleResposity.findByUserContaining(userId, pageable);
        }

        responseInfo.setCode(200);
        responseInfo.setMsg("success");
        responseInfo.setFlag(true);
        responseInfo.setTotalPageCount(currentEntityList.getTotalPages());
        responseInfo.setTotalCount((int) currentEntityList.getTotalElements());
        responseInfo.setHasMore(currentEntityList.hasNext());
        responseInfo.setData(currentEntityList.stream().toList());
        return responseInfo;
    }


    /**
     * 获取文章详情
     * @param id
     * @return
     */
    @JwtToken
    @GetMapping("/article/detail")
    public @ResponseBody ResponseInfo ArticleDetail(@RequestParam("articleId")String id) {

        ResponseInfo responseInfo = new ResponseInfo();
       Optional<ArticleEntity> result = articleResposity.findById(id);
       if (result.isPresent()) {
           responseInfo.setData(result.get());
           responseInfo.setCode(200);
       }else{
           responseInfo.setCode(500);
       }
                return responseInfo;
    }

    /**
     * 修改文章类型：public ，private
     * @param articleInfo
     * @return
     */
    @JwtToken
    @PostMapping("/article/changeType")
    public @ResponseBody ResponseInfo updateArticle(@RequestBody Map articleInfo) {
        ResponseInfo responseInfo = new ResponseInfo();
        String articleId = (String) articleInfo.get("articleId");
        ArticleEntity articleEntity = articleResposity.findByArticleId(articleId);
        if (articleEntity == null) {
            responseInfo.setCode(500);
            responseInfo.setMsg("no article found");
        } else {
            int previousType = articleEntity.getType();
            if (previousType == 0) {
                articleEntity.setType(1);
            } else {
                articleEntity.setType(0);
            }
            responseInfo.setCode(200);
            responseInfo.setMsg("success");
            responseInfo.setData(articleEntity);
            articleResposity.save(articleEntity);
        }
        return responseInfo;
    }

    /**
     *  新建文章
     * @param articleDto
     * @return
     */
    @JwtToken
    @PostMapping("/new/article")
    public @ResponseBody ResponseInfo createArticle(@RequestBody ArticleDto articleDto) {

        String userId = ServiceSupport.getCurrentUserId();
        Optional<UserEntity> result = userRepository.findById(userId);
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setTitle(articleDto.getTitle());
        articleEntity.setContent(articleDto.getContent());
        articleEntity.setType(1);
        articleEntity.setUserId(ServiceSupport.getCurrentUserId());
        if (result != null) {
            UserEntity entity = result.get();
            articleEntity.setAuthor(entity.getNickname());
            articleEntity.setAvartarUrl(entity.getAvatarUrl());
        }
        long currentTime = System.currentTimeMillis();
        articleEntity.setCreateTime(currentTime);
        articleEntity.setUpdateTime(currentTime);

        articleResposity.save(articleEntity);
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setCode(200);
        responseInfo.setMsg("succeed");
        responseInfo.setFlag(true);
        responseInfo.setData(articleEntity);
        return responseInfo;
    }

    @JwtToken
    @PostMapping("/delete/article")
    public @ResponseBody ResponseInfo deleteArticle(@RequestBody Map articleInfo) {
        ResponseInfo responseInfo = new ResponseInfo();
        String articleId = (String) articleInfo.get("articleId");
        ArticleEntity articleEntity = articleResposity.findByArticleId(articleId);
        if (articleEntity == null) {
            responseInfo.setCode(500);
            responseInfo.setMsg("no article found");
        } else {
            articleResposity.deleteById(articleId);
            responseInfo.setCode(200);
            responseInfo.setMsg("article deleted");
        }
        return responseInfo;
    }
}


//        String[] imgUrlArr = articleDto.getImgUrl();
//        StringBuilder imgUrls = new StringBuilder();
//        if (imgUrlArr != null && imgUrlArr.length > 0) {
//            imgUrls = new StringBuilder(imgUrlArr[0]);
//            for (int i = 1; i < imgUrlArr.length; i++) {
//                imgUrls.append(",").append(imgUrlArr[i]);
//            }
//        }
//
//        articleEntity.setImgUrl(imgUrls.toString());