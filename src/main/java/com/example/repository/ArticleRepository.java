package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.ArticleResultSetExtractor;

/**
 * articlesテーブルを操作するリポジトリ.
 * 
 * @author kohei.takasaki
 *
 */
@Repository
public class ArticleRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * コメントも含めた記事をID, コメントIDの降順で全件取得する.
	 * 
	 * @return すべての記事のリスト
	 */
	public List<Article> findAll() {
		String sql = "SELECT\r\n" + 
				"  a.id as id\r\n" + 
				", a.name as name\r\n" + 
				", a.content as content\r\n" + 
				", c.id as com_id\r\n" + 
				", c.name as com_name\r\n" + 
				", c.content as com_content\r\n" + 
				", c.article_id as article_id\r\n" + 
				"FROM\r\n" + 
				"  articles a\r\n" + 
				"  LEFT OUTER JOIN\r\n" + 
				"    comments c\r\n" + 
				"    ON a.id = c.article_id\r\n" + 
				"ORDER BY\r\n" + 
				"  a.id DESC\r\n" + 
				", c.id ASC\r\n" + 
				";";
		
		ArticleResultSetExtractor rowMapper = new ArticleResultSetExtractor();
		
		List<Article> articleList = template.query(sql, rowMapper);
		return articleList;
	}

	/**
	 * 引数の記事をarticleテーブルにインサートする.
	 * 
	 * @param article インサートする記事情報
	 */
	public void insert(Article article) {
		String sql = "INSERT INTO\r\n" + "  articles(name, content)\r\n" + "VALUES\r\n" + "  (:name, :content)\r\n"
				+ ";";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", article.getName()).addValue("content",
				article.getContent());
		int insNum = template.update(sql, param);
		System.out.println(insNum + "件の記事を挿入しました");
	}

	/**
	 * 引数のIDと一致する記事と記事のコメントをすべて削除する.
	 * 
	 * @param id ID
	 */
	public void deleteById(int id) {
		String sql = "DELETE\r\n" + "FROM\r\n" + "  articles\r\n" + "WHERE\r\n" + "  id = :id\r\n" + ";";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		int delNum = template.update(sql, param);
		System.out.println(delNum + "件の記事を削除しました");
	}
}
