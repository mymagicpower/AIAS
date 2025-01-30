package top.aias.training.controller;

import lombok.RequiredArgsConstructor;
import top.aias.training.domain.ResultBean;
import top.aias.training.domain.TrainArgument;
import top.aias.training.service.TrainArgumentService;
import org.springframework.web.bind.annotation.*;

/**
 * 超参数服务
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@RestController
@RequiredArgsConstructor
//@Api(tags = "超参数管理")
@RequestMapping("/api/trainArgument")
public class TrainArgumentController {
    private final TrainArgumentService trainArgumentService;

    /**
     * 查看超参数
     * @return
     */
    @GetMapping(value="/info",produces="application/json;charset=utf-8")
    public ResultBean trainArgument() {
        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        return ResultBean.success().add("result", trainArgument);
    }

    /**
     * 更新超参数
     * @param trainArgument
     * @return
     */
    @PostMapping("/update")
    public ResultBean update(@RequestBody TrainArgument trainArgument) {
        trainArgumentService.update(trainArgument);
        return ResultBean.success();
    }

}