package me.aias.example.utils;

import ai.djl.Model;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.util.NDImageUtils;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Batchifier;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableStructTranslator implements Translator<Image, TableResult> {

  private final int maxLength;
  private int height;
  private int width;
  private float xScale = 1.0f;
  private float yScale = 1.0f;

  public TableStructTranslator(Map<String, ?> arguments) {
    maxLength =
        arguments.containsKey("maxLength")
            ? Integer.parseInt(arguments.get("maxLength").toString())
            : 488;
  }

  private Map<String, String> dict_idx_character = new ConcurrentHashMap<>();
  private Map<String, String> dict_character_idx = new ConcurrentHashMap<>();
  private Map<String, String> dict_idx_elem = new ConcurrentHashMap<>();
  private Map<String, String> dict_elem_idx = new ConcurrentHashMap<>();
  private String beg_str = "sos";
  private String end_str = "eos";

  @Override
  public void prepare(TranslatorContext ctx) throws IOException {
    Model model = ctx.getModel();
    //    ppocr_keys_v1.txt
    try (InputStream is = model.getArtifact("table_structure_dict.txt").openStream()) {
      List<String> lines = Utils.readLines(is, false);
      String[] substr = lines.get(0).trim().split("\\t");
      int characterNum = Integer.parseInt(substr[0]);
      int elemNum = Integer.parseInt(substr[1]);

      List<String> listCharacter = new ArrayList<>();
      List<String> listElem = new ArrayList<>();
      for (int i = 1; i < 1 + characterNum; i++) {
        listCharacter.add(lines.get(i).trim());
      }
      for (int i = 1 + characterNum; i < 1 + characterNum + elemNum; i++) {
        listElem.add(lines.get(i).trim());
      }
      listCharacter.add(0, beg_str);
      listCharacter.add(end_str);
      listElem.add(0, beg_str);
      listElem.add(end_str);

      for (int i = 0; i < listCharacter.size(); i++) {
        dict_idx_character.put("" + i, listCharacter.get(i));
        dict_character_idx.put(listCharacter.get(i), "" + i);
      }
      for (int i = 0; i < listElem.size(); i++) {
        dict_idx_elem.put("" + i, listElem.get(i));
        dict_elem_idx.put(listElem.get(i), "" + i);
      }
    }
  }

  @Override
  public NDList processInput(TranslatorContext ctx, Image input) {
    NDArray img = input.toNDArray(ctx.getNDManager(), Image.Flag.COLOR);
    height = input.getHeight();
    width = input.getWidth();

    //    img = ResizeTableImage(img, height, width, maxLength);
    //    img = PaddingTableImage(ctx, img, maxLength);

    img = NDImageUtils.resize(img, 488, 488);
    
    // img = NDImageUtils.toTensor(img);
    img = img.transpose(2, 0, 1).div(255).flip(0);
    img =
        NDImageUtils.normalize(
            img, new float[] {0.485f, 0.456f, 0.406f}, new float[] {0.229f, 0.224f, 0.225f});

    img = img.expandDims(0);
    return new NDList(img);
  }

  @Override
  public TableResult processOutput(TranslatorContext ctx, NDList list) {
    NDArray locPreds = list.get(0);
    NDArray structureProbs = list.get(1);
    NDArray structure_idx = structureProbs.argMax(2);
    NDArray structure_probs = structureProbs.max(new int[] {2});

    List<List<String>> result_list = new ArrayList<>();
    List<List<String>> result_pos_list = new ArrayList<>();
    List<List<String>> result_score_list = new ArrayList<>();
    List<List<String>> result_elem_idx_list = new ArrayList<>();
    List<String> res_html_code_list = new ArrayList<>();
    List<NDArray> res_loc_list = new ArrayList<>();

    // get ignored tokens
    int beg_idx = Integer.parseInt(dict_elem_idx.get(beg_str));
    int end_idx = Integer.parseInt(dict_elem_idx.get(end_str));

    long batch_size = structure_idx.size(0); // len(text_index)
    for (int batch_idx = 0; batch_idx < batch_size; batch_idx++) {
      List<String> char_list = new ArrayList<>();
      List<String> elem_pos_list = new ArrayList<>();
      List<String> elem_idx_list = new ArrayList<>();
      List<String> score_list = new ArrayList<>();

      long len = structure_idx.get(batch_idx).size();
      for (int idx = 0; idx < len; idx++) {
        int tmp_elem_idx = (int) structure_idx.get(batch_idx).get(idx).toLongArray()[0];
        if (idx > 0 && tmp_elem_idx == end_idx) {
          break;
        }
        if (tmp_elem_idx == beg_idx || tmp_elem_idx == end_idx) {
          continue;
        }

        char_list.add(dict_idx_elem.get("" + tmp_elem_idx));
        elem_pos_list.add("" + idx);
        score_list.add("" + structure_probs.get(batch_idx, idx).toFloatArray()[0]);
        elem_idx_list.add("" + tmp_elem_idx);
      }

      result_list.add(char_list); // structure_str
      result_pos_list.add(elem_pos_list);
      result_score_list.add(score_list);
      result_elem_idx_list.add(elem_idx_list);
    }

    int batch_num = result_list.size();
    for (int bno = 0; bno < batch_num; bno++) {
      NDList res_loc = new NDList();
      int len = result_list.get(bno).size();
      for (int sno = 0; sno < len; sno++) {
        String text = result_list.get(bno).get(sno);
        if (text.equals("<td>") || text.equals("<td")) {
          int pos = Integer.parseInt(result_pos_list.get(bno).get(sno));
          res_loc.add(locPreds.get(bno, pos));
        }
      }

      String res_html_code = StringUtils.join(result_list.get(bno), "");
      res_html_code_list.add(res_html_code);
      NDArray array = NDArrays.stack(res_loc);
      res_loc_list.add(array);
    }

    // structure_str_list result_list
    // res_loc            res_loc_list
    List<BoundingBox> boxes = new ArrayList<>();

    long rows = res_loc_list.get(0).size(0);
    for (int rno = 0; rno < rows; rno++) {
      float[] arr = res_loc_list.get(0).get(rno).toFloatArray();
      Rectangle rect = new Rectangle(arr[0], arr[1], (arr[2] - arr[0]), (arr[3] - arr[1]));
      boxes.add(rect);
    }

    List<String> structure_str_list = result_list.get(0);
    structure_str_list.add(0, "<table>");
    structure_str_list.add(0, "<body>");
    structure_str_list.add(0, "<html>");
    structure_str_list.add("</table>");
    structure_str_list.add("</body>");
    structure_str_list.add("</html>");

    TableResult result = new TableResult(structure_str_list, boxes);

    return result;
  }

  @Override
  public Batchifier getBatchifier() {
    return null;
  }

  private NDArray ResizeTableImage(NDArray img, int height, int width, int maxLen) {
    int localMax = Math.max(height, width);
    float ratio = maxLen * 1.0f / localMax;
    int resize_h = (int) (height * ratio);
    int resize_w = (int) (width * ratio);
    if(width > height){
      xScale = 1.0f;
      yScale = ratio;
    } else{
      xScale = ratio;
      yScale = 1.0f;
    }

    img = NDImageUtils.resize(img, resize_w, resize_h);
    return img;
  }

  private NDArray PaddingTableImage(TranslatorContext ctx, NDArray img, int maxLen) {

    Image srcImg = ImageFactory.getInstance().fromNDArray(img.duplicate());
    saveImage(srcImg, "img.png", "build/output");

    NDArray paddingImg = ctx.getNDManager().zeros(new Shape(maxLen, maxLen, 3), DataType.UINT8);
    //    NDManager manager = NDManager.newBaseManager();
    //    NDArray paddingImg = manager.zeros(new Shape(maxLen, maxLen, 3), DataType.UINT8);
    paddingImg.set(
        new NDIndex("0:" + img.getShape().get(0) + ",0:" + img.getShape().get(1) + ",:"), img);
    Image image = ImageFactory.getInstance().fromNDArray(paddingImg);

    saveImage(image, "paddingImg.png", "build/output");

    return paddingImg;
  }

  public void saveImage(Image img, String name, String path) {
    Path outputDir = Paths.get(path);
    Path imagePath = outputDir.resolve(name);
    // OpenJDK 不能保存 jpg 图片的 alpha channel
    try {
      img.save(Files.newOutputStream(imagePath), "png");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
