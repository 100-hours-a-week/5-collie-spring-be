package com.collieReact.service;

import com.collieReact.entity.Post;
import com.collieReact.entity.Comment;
import com.collieReact.repository.PostRepository;
import com.collieReact.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FileStorageService fileStorageService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, FileStorageService fileStorageService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(int postId) {
        return postRepository.findById(postId);
    }

    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }

    public void createPost(Post post, MultipartFile file) {
        String fileName = fileStorageService.store(file);
        String fileUrl = fileStorageService.getFileUrl(fileName);
        post.setPostPicture(fileUrl);
        postRepository.save(post);
    }

    public void updatePost(Post post, MultipartFile file) {
        String fileName = fileStorageService.store(file);
        String fileUrl = fileStorageService.getFileUrl(fileName);
        post.setPostPicture(fileUrl);
        postRepository.update(post);
    }



    public void updatePostWithoutFile(Post post, String postPicture) {
        if (postPicture != null && !postPicture.isEmpty()) {
            post.setPostPicture(postPicture);
        }
        postRepository.update(post);
    }

    public void incrementPostViews(Long postId) {
        postRepository.incrementViews(postId);
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId.intValue());
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void updateComment(Comment comment) {
        commentRepository.update(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId.intValue());
    }
}