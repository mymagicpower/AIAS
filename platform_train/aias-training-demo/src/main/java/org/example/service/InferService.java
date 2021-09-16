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
package org.example.service;

import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import org.example.inference.ImageClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author Calvin
 * @date Apr 05, 2021
 */
@Service
public class InferService {

    private Logger logger = LoggerFactory.getLogger(InferService.class);
    
	@Value("${model.mnist}")
	private String mnistModelPath;

	public String getImageInfo(InputStream inputStream) {
		try {
			Image img = ImageFactory.getInstance().fromInputStream(inputStream);
			Classifications classifications = ImageClassification.predict(img);
			return classifications.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public String getImageInfoForUrl(String imageUrl) {
		try {
			Image img = ImageFactory.getInstance().fromUrl(imageUrl);
			Classifications classifications = ImageClassification.predict(img);
			return classifications.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
