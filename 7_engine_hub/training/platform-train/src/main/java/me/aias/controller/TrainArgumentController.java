package me.aias.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.aias.domain.ResultBean;
import me.aias.domain.TrainArgument;
import me.aias.service.TrainArgumentService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Calvin
 * @date 2021-06-20
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "超参数管理")
@RequestMapping("/api/trainArgument")
public class TrainArgumentController {
    private final TrainArgumentService trainArgumentService;

    @ApiOperation("查看超参数")
    @GetMapping(value="/info",produces="application/json;charset=utf-8")
    public ResultBean trainArgument() {
        TrainArgument trainArgument = trainArgumentService.getTrainArgument();
        return ResultBean.success().add("result", trainArgument);
    }
    
    @ApiOperation("修改超参数")
    @PostMapping("/update")
    public ResultBean update(@RequestBody TrainArgument trainArgument) {
        trainArgumentService.update(trainArgument);
        return ResultBean.success();
    }

}