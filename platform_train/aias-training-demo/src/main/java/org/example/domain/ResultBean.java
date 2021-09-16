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
package org.example.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String value;
    private Map<String, Object> data = new HashMap<String, Object>();

    public static ResultBean success() {
        ResultBean rb = new ResultBean();
        rb.setCode(0);
        rb.setValue("Success");
        return rb;
    }

    public static ResultBean failure() {
        ResultBean msg = new ResultBean();
        msg.setCode(-1);
        msg.setValue("Failure");
        return msg;
    }

    public ResultBean() {

    }

    public ResultBean add(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
