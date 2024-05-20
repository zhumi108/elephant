package com.tt.elephant.model;

import lombok.Data;

@Data
public class ArticleResponseInfo extends ResponseInfo {

    public Object data;

    public int totalPageCount;

    public int totalCount;

    public boolean hasMore;
}
