package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.service.CommentService;

/**
 * コメント情報についてのコントローラ.
 * 
 * @author kohei.takasaki
 *
 */
@Controller
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentService commentServ;

	@Autowired
	private BBSController bbsController;

	/**
	 * 記事投稿フォームのインスタンス化.
	 * 
	 * @return 記事投稿フォーム
	 */
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}

	/**
	 * コメント投稿フォームのインスタンス化.
	 * 
	 * @return コメント投稿フォーム
	 */
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}

	@RequestMapping("insert")
	public String insert(Integer articleId, @Validated CommentForm form, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		if (result.hasErrors()) {
			return "redirect:/";
		}
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(articleId);
		commentServ.insert(comment);
		return "redirect:/";
	}

}
