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

import org.apache.commons.codec.binary.Base64;
import org.example.domain.ResultBean;
import org.example.service.InferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/inference")
public class InferController {
	private Logger logger = LoggerFactory.getLogger(InferController.class);

	@Autowired
	private InferService inferService;

	@GetMapping(value="/mnistImageUrl",produces="application/json;charset=utf-8")
	public ResultBean mnistImageUrl(@RequestParam(value = "url") String url) {
		String result = inferService.getImageInfoForUrl(url);
		return ResultBean.success().add("result", result);
	}

	@PostMapping("/mnistImage")
	public ResultBean mnistImage(@RequestParam(value = "imageFile") MultipartFile imageFile) {
		InputStream fis = null;
		try {
			InputStream ins = imageFile.getInputStream();
			String result = inferService.getImageInfo(ins);
			String base64Img = Base64.encodeBase64String(imageFile.getBytes());
			return ResultBean.success().add("result", result)
					.add("base64Img","data:image/jpeg;base64," + base64Img);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return ResultBean.failure().add("errors", e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}
