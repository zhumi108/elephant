package com.tt.elephant.controller;

import com.tt.elephant.model.ArticleDto;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.repository.ArticleEntity;
import com.tt.elephant.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleResposity;

    /**
     * 获取文章列表
     * @param author
     * @return
     */
    @GetMapping("/article/list")
    public @ResponseBody ResponseInfo ArticleList(@RequestParam("author") String author, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize){
        ResponseInfo responseInfo = new ResponseInfo();

        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<ArticleEntity> articleEntityList = articleResposity.findByAuthorContaining(author,pageable);
        responseInfo.setCode("true");
        responseInfo.setData(articleEntityList);
        return responseInfo;
    }


    /**
     * 获取文章详情
     * @param id
     * @return
     */
    @GetMapping("/article/detail")
    public @ResponseBody ResponseInfo ArticleDetail(@RequestParam("articleId")String id) {

        ResponseInfo responseInfo = new ResponseInfo();
       Optional<ArticleEntity> result = articleResposity.findById(id);
       if (result.isPresent()) {
           responseInfo.setData(result.get());
           responseInfo.setCode("true");
       }else{
           responseInfo.setCode("false");
       }
                return responseInfo;
    }

    /**
     * 修改文章类型：public ，private
     * @param articleDto
     * @return
     */
    @PostMapping("/article/changeType")
    public @ResponseBody ResponseInfo updateArticle(@RequestBody ArticleDto articleDto) {
        ResponseInfo responseInfo = new ResponseInfo();
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(articleDto.getArticleId());
        articleEntity.setStatus(articleDto.getStatus());
        articleResposity.save(articleEntity);
        responseInfo.setCode("true");
        return responseInfo;
    }

    /**
     *  新建文章
     * @param articleDto
     * @return
     */
    @PostMapping("/new/article")
    public @ResponseBody ResponseInfo createArticle(@RequestBody ArticleDto articleDto) {

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setAuthor(articleDto.getAuthor());
        articleEntity.setTitle(articleDto.getTitle());
        articleEntity.setContent(articleDto.getContent());
        articleEntity.setStatus(articleDto.getStatus());
        articleEntity.setImgUrl(articleDto.getImgUrl());


       ArticleEntity articleEntity1= articleResposity.save(articleEntity);
       ResponseInfo responseInfo = new ResponseInfo();

           responseInfo.setCode("true");

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