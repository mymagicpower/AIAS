package me.calvin.modules.search.service;

import me.calvin.modules.search.domain.ImageInfo;
import me.calvin.modules.search.service.dto.ImageInfoDto;
import me.calvin.modules.search.service.dto.ImageInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图像信息服务接口
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 **/
public interface ImageInfoService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ImageInfoQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ImageInfoDto>
     */
    List<ImageInfoDto> queryAll(ImageInfoQueryCriteria criteria);

    /**
     * 查询所有数据不分页
     *
     * @param id 条件参数
     * @return List<ImageInfoDto>
     */
    List<ImageInfoDto> queryByGroupId(Long id);
    List<ImageInfoDto> queryAll(Long id);
    List<ImageInfoDto> queryAll(ArrayList<Long> idList);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ImageInfoDto
     */
    ImageInfoDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return ImageInfoDto
     */
    ImageInfoDto create(ImageInfo resources);

    /**
     * 创建一组图片信息
     *
     * @param imageInfoList /
     * @return List<ImageInfoDto>
     */
    List<ImageInfoDto> createAll(List<ImageInfo> imageInfoList);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(ImageInfo resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ImageInfoDto> all, HttpServletResponse response) throws IOException;

    /**
     * 保存base64图片信息
     */
//    ImageInfo uploadBase64Image(String base64Image, String imageRootPath, String relativePath) throws IOException;

    /**
     * 保存url图片信息
     */
    ImageInfo uploadImageByUrl(String url, String imageRootPath, String relativePath) throws IOException;

    /**
     * 保存图片信息
     */
    ImageInfo uploadImage(MultipartFile imageFile, String imageRootPath, String relativePath) throws IOException;

    /**
     * 保存zip包图片信息
     */
    List<ImageInfo> uploadImages(String sourcePath, String imageRootPath, String relativePath, boolean delete) throws IOException;
}