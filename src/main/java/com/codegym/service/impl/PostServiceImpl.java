package com.codegym.service.impl;

import com.codegym.model.*;
import com.codegym.repository.PostsRepository;
import com.codegym.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl extends ValidateService implements PostsService {
    @Autowired
    private PostsRepository postsRepository;


    @Override
    public List<Posts> findAllByIsDeleteIsFalseOrderByIdDesc() {
        return postsRepository.findAllByIsDeleteIsFalseOrderByIdDesc();
    }

    @Override
    public List<Posts> findAllByCategory(Category category) {
        return postsRepository.findAllByCategory(category);
    }

    @Override
    public ArrayList<String> listResult(Posts posts) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(posts.getTitle(), 10, 200)) {
            listError.add("Lỗi nhập Tiêu đề");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }

    @Override
    public ArrayList<String> insert(Posts posts) throws SQLException {
        ArrayList<String> listError = listResult(posts);
        ArrayList<Posts> listPosts = new ArrayList<>(findAllByIsDeleteIsFalseOrderByIdDesc());
        for (Posts posts1 : listPosts) {
            if (posts1.getTitle().equals(posts.getTitle())) {
                listError.add("Tiêu đề bài viết đã được đăng");
            }
            if (posts1.getShortContent().equals(posts.getShortContent())) {
                listError.add("Nội dung tóm tắt đã được sử dụng");
            }
            if (posts1.getFullContent().equals(posts.getFullContent())) {
                listError.add("Nội dung bài viết đã bị trùng");
            }
        }

        if (listError.size() == 1 && listError.get(0).equals("Thành công")) {
            if (checkDuplicate(posts)) {
                listError.set(0, "Bài viết đã được sử dụng");
            } else {
                this.postsRepository.save(posts);
            }
        } else {
            listError.remove("Thành công");
        }

        return listError;
    }

    @Override
    public ArrayList<String> update(Posts posts) throws SQLException {
        ArrayList<String> listError = listResult(posts);
        Optional<Posts> posts1 = postsRepository.findById(posts.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (posts1.get().getTitle().equals(posts.getTitle())) {
                listError.set(0, "Không thực hiện chỉnh sửa gì");
            } else if (checkUpdate(posts)) {
                listError.set(0, "Nội dung chỉnh sửa đã bị trùng");
            } else {
                postsRepository.save(posts);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(Posts posts) throws SQLException {
        ArrayList<String> listError = listResult(posts);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            posts.setDelete(true);
            postsRepository.save(posts);
            listError.set(0, "Xóa thành công");
        } else {
            listError.set(0, "Xóa không thành công");
        }
        return listError;
    }

    @Override
    public Optional<Posts> findById(int id) throws SQLException {
        return postsRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(Posts posts) throws SQLException {
        ArrayList<Posts> postsArrayList = new ArrayList<>(findAllByIsDeleteIsFalseOrderByIdDesc());
        for (Posts posts1 : postsArrayList) {
            if (posts1.getTitle().equals(posts.getTitle()) &&
                    posts1.getShortContent().equals(posts.getShortContent()) &&
                    posts1.getFullContent().equals(posts.getFullContent()) &&
                    posts1.getImageUrl().equals(posts.getImageUrl())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<Posts> findAllByCategoryIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable) {
        return postsRepository.findAllByCategoryIdAndIsDeleteIsFalseOrderByPublishDateDesc(id,pageable);
    }

    @Override
    public Page<Posts> findAllByIsDeleteIsFalse(Pageable pageable) {
        return postsRepository.findAllByIsDeleteIsFalse(pageable);
    }

    public boolean checkUpdate(Posts posts) throws SQLException {
        ArrayList<Posts> postsArrayList = new ArrayList<>(findAllByIsDeleteIsFalseOrderByIdDesc());
        for (Posts posts1 : postsArrayList) {
            if (posts1.getTitle().equals(posts.getTitle()) &&
                    posts1.getShortContent().equals(posts.getShortContent()) &&
                    posts1.getFullContent().equals(posts.getFullContent()) &&
                    posts1.getImageUrl().equals(posts.getImageUrl()) &&
                    posts1.getImageUrl().equals(posts.getImageUrl())) {
                return true;
            }
        }
        return false;
    }
}
