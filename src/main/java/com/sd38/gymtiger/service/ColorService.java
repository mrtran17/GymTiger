package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.Color;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ColorService {
    List<Color> getAll();

    Page<Color> getPage(Integer page);

    Boolean add(String name);

    Boolean update(Integer id, String name);

    Color delete(Integer id);

    Page<Color> search(String name, int page);

    Color detail(Integer id);

    Page<Color> getAllSizeDelete(int page);

    Color restore(Integer id);

    Page<Color> searchDeleted(String name, int page);

    String importExcel(MultipartFile file) throws IOException;
}
