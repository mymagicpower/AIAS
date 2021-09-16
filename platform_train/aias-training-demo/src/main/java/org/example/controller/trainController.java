/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.example.controller;

import org.example.domain.ResultBean;
import org.example.service.TrainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/train")
public class trainController {
	private Logger logger = LoggerFactory.getLogger(trainController.class);

	@Autowired
	private TrainService trainService;

	@GetMapping(value="/trigger",produces="application/json;charset=utf-8")
	public ResultBean train(@RequestParam(value = "id") String id) {
		try {
			trainService.train(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResultBean.failure();
		}
		return ResultBean.success();
	}
}
