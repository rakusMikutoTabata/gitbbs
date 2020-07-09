package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Comment;

/**
 * commentsテーブルを操作するリポジトリ.
 * 
 * @author kohei.takasaki
 *
 */
@Repository
public class CommentRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		comment.setName(rs.getString("name"));
		comment.setContent(rs.getString("content"));
		comment.setArticleId(rs.getInt("article_id"));
		return comment;
	};
	
	/**
	 * 指定した記事のIDと一致するコメントをIDの降順ですべて取得する.
	 * 
	 * @param articleId 記事のID
	 * @return 記事IDと一致するコメント情報のリスト
	 */
	public List<Comment> findByArticleId(int articleId) {
		String sql = "SELECT\r\n" + 
				"  id\r\n" + 
				", name\r\n" + 
				", content\r\n" + 
				", article_id\r\n" + 
				"FROM\r\n" + 
				"  comments\r\n" + 
				"WHERE\r\n" + 
				"  article_id = :articleId\r\n" + 
				"ORDER BY\r\n" + 
				"  id DESC\r\n" + 
				";";
		SqlParameterSource param = new MapSqlParameterSource()
										.addValue("articleId", articleId);
		List<Comment> commentList = template.query(sql, param, COMMENT_ROW_MAPPER);
		return commentList;
	}
	
	/**
	 * 引数のコメントをcommentsテーブルにインサートする.
	 * 
	 * @param comment インサートするコメント情報
	 */
	public void insert(Comment comment) {
		String sql = "INSERT INTO\r\n" + 
				"  comments(name, content, article_id)\r\n" + 
				"VALUES\r\n" + 
				"  (:name, :content, :articleId)\r\n" + 
				";";
		SqlParameterSource param = new MapSqlParameterSource()
									.addValue("name", comment.getName())
									.addValue("content", comment.getContent())
									.addValue("articleId", comment.getArticleId());
		int insNum = template.update(sql, param);
		System.out.println(insNum + "件のコメントを挿入しました");
	}
	
	/**
	 * 引数の記事IDと一致するコメントをすべて削除する.
	 * 
	 * @param articleId 記事ID
	 */
	public void deleteByArticleId(int articleId) {
		String sql = "DELETE\r\n" + 
				"FROM\r\n" + 
				"  comments\r\n" + 
				"WHERE\r\n" + 
				"  article_id = :articleId\r\n" + 
				";";
		SqlParameterSource param = new MapSqlParameterSource()
										.addValue("articleId", articleId);
		int delNum = template.update(sql, param);
		System.out.println(delNum + "件のコメントを削除しました");
	}
	
}
