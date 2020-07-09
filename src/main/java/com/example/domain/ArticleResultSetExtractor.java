package com.example.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;


/**
 * Article用のマッパー.
 * 
 * @author kohei.takasaki
 *
 */
public class ArticleResultSetExtractor implements ResultSetExtractor<List<Article>> {

	@Override
	public List<Article> extractData(ResultSet rs) throws SQLException, DataAccessException {
		LinkedHashMap<Integer, Article> articleMap = new LinkedHashMap<>();
		
		while(rs.next()) {
			Article article = null;
			Integer articleId = rs.getInt("id");
			if(articleMap.containsKey(articleId)) {
				article = articleMap.get(articleId);
			} else {
				article = new Article();
				article.setId(articleId);
				article.setName(rs.getString("name"));
				article.setContent(rs.getString("content"));
				article.setCommentList(new ArrayList<>());
				articleMap.put(articleId, article);
			}
			
			Integer commentId = rs.getInt("com_id");
			if (rs.wasNull()) {
				continue;
			}
			
			List<Comment> commentList = article.getCommentList();
			Comment comment = new Comment();
			comment.setId(commentId);
			comment.setName(rs.getString("com_name"));
			comment.setContent(rs.getString("com_content"));
			comment.setArticleId(articleId);
			commentList.add(comment);
			article.setCommentList(commentList);
			articleMap.put(articleId, article);
		}
		return new ArrayList<>(articleMap.values());
	}

}
