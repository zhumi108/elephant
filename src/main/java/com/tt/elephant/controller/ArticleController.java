package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.ArticleDto;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.repository.ArticleEntity;
import com.tt.elephant.repository.ArticleRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "文章管理接口", description = "文章管理")
@RequestMapping("/api/v1")
@RestController
public class ArticleController {
    @Autowired
    ArticleRepository articleResposity;

    /**
     * 获取文章列表
     * @param author
     * @return
     */
    @JwtToken
    @GetMapping("/article/list")
    public @ResponseBody ResponseInfo ArticleList(@RequestParam("author") String author, @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize){
        ResponseInfo responseInfo = new ResponseInfo();

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);

        Page<ArticleEntity> articleEntityList = articleResposity.findByAuthorContaining(author,pageable);
        responseInfo.setCode(200);
        responseInfo.setData(articleEntityList);
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
     * @param articleDto
     * @return
     */
    @JwtToken
    @PostMapping("/article/changeType")
    public @ResponseBody ResponseInfo updateArticle(@RequestBody ArticleDto articleDto) {
        ResponseInfo responseInfo = new ResponseInfo();
        ArticleEntity articleEntity = new ArticleEntity();
//        articleEntity.setArticleId(articleDto.getArticleId());
//        articleEntity.setStatus(articleDto.getStatus());
        articleResposity.save(articleEntity);
        responseInfo.setCode(200);
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

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setAuthor("Jeremy");
        articleEntity.setTitle(articleDto.getTitle());
        articleEntity.setContent(articleDto.getContent());
        articleEntity.setType(0);
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