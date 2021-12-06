package me.calvin.modules.search.service;

import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @description 服务接口
* @author Calvin
* @date 2021-02-17
**/
public interface ImageInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ImageInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ImageInfoDto>
    */
    List<ImageInfoDto> queryAll(ImageInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ImageInfoDto
     */
    ImageInfoDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return ImageInfoDto
    */
    ImageInfoDto create(ImageInfo resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(ImageInfo resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<ImageInfoDto> all, HttpServletResponse response) throws IOException;

    /** 保存base64图片信息 */
    ImageInfo uploadBase64Image(String base64Image, String imageRootPath) throws IOException;
    /** 保存base64图片信息 */
    List<ImageInfo> uploadBase64Images(HashMap<String, String> urls, String imageRootPath) throws IOException;
    /** 保存url图片信息 */
    ImageInfo uploadImageByUrl(String url, String imageRootPath, String save) throws IOException;
    /** 保存url图片信息 */
    List<ImageInfo> uploadImageByUrls(HashMap<String, String> urls, String imageRootPath, String save) throws IOException;
    /** 保存图片信息 */
    ImageInfo uploadImage(MultipartFile imageFile, String imageRootPath) throws IOException;
    /** 保存zip包图片信息 */
    List<ImageInfo> uploadImages(String uploadPath, String imageRootPath) throws IOException;
}