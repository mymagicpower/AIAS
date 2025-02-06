/*
 * Copyright 2024 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package top.aias.platform.bean;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.Point;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.ndarray.NDList;
import ai.djl.util.JsonUtils;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class represents the segment anything input.
 */
public final class Sam2Input {

    private Image image;
    private Point[] points;
    private int[] labels;
    private boolean visualize;
    private NDList embeddings;

    /**
     * Constructs a {@code Sam2Input} instance.
     *
     * @param image  the image
     * @param points the locations on the image
     * @param labels the labels for the locations (0: background, 1: foreground)
     */
    public Sam2Input(Image image, Point[] points, int[] labels) {
        this(image, points, labels, false);
    }

    /**
     * Constructs a {@code Sam2Input} instance.
     *
     * @param image     the image
     * @param points    the locations on the image
     * @param labels    the labels for the locations (0: background, 1: foreground)
     * @param visualize true if output visualized image
     */
    public Sam2Input(Image image, Point[] points, int[] labels, boolean visualize) {
        this.image = image;
        this.points = points;
        this.labels = labels;
        this.visualize = visualize;
    }

    public NDList getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(NDList embeddings) {
        this.embeddings = embeddings;
    }

    /**
     * Returns the image.
     *
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns {@code true} if output visualized image.
     *
     * @return {@code true} if output visualized image
     */
    public boolean isVisualize() {
        return visualize;
    }

    /**
     * Returns the locations.
     *
     * @return the locations
     */
    public List<Point> getPoints() {
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < labels.length; ++i) {
            if (labels[i] < 2) {
                list.add(points[i]);
            }
        }
        return list;
    }

    /**
     * Returns the box.
     *
     * @return the box
     */
    public List<Rectangle> getBoxes() {
        List<Rectangle> list = new ArrayList<>();
        for (int i = 0; i < labels.length; ++i) {
            if (labels[i] == 2) {
                double width = points[i + 1].getX() - points[i].getX();
                double height = points[i + 1].getY() - points[i].getY();
                list.add(new Rectangle(points[i], width, height));
            }
        }
        return list;
    }

    public float[] toLocationArray(int width, int height) {
        float[] ret = new float[points.length * 2];
        int i = 0;
        for (Point point : points) {
            ret[i++] = (float) point.getX() / width * 1024;
            ret[i++] = (float) point.getY() / height * 1024;
        }
        return ret;
    }

    public float[][] getLabels() {
        float[][] buf = new float[1][labels.length];
        for (int i = 0; i < labels.length; ++i) {
            buf[0][i] = labels[i];
        }
        return buf;
    }

    /**
     * Constructs a {@code Sam2Input} instance from json string.
     *
     * @param input the json input
     * @return a {@code Sam2Input} instance
     * @throws IOException if failed to load the image
     */
    public static Sam2Input fromJson(String input) throws IOException {
        Prompt prompt = JsonUtils.GSON.fromJson(input, Prompt.class);
        if (prompt.image == null) {
            throw new IllegalArgumentException("Missing image_url value");
        }
        if (prompt.prompt == null || prompt.prompt.length == 0) {
            throw new IllegalArgumentException("Missing prompt value");
        }
        Image image = ImageFactory.getInstance().fromUrl(prompt.image);
        Builder builder = builder(image);
        if (prompt.visualize) {
            builder.visualize();
        }
        for (Location location : prompt.prompt) {
            int[] data = location.data;
            if ("point".equals(location.type)) {
                builder.addPoint(data[0], data[1], location.label);
            } else if ("rectangle".equals(location.type)) {
                builder.addBox(data[0], data[1], data[2], data[3]);
            }
        }
        return builder.build();
    }

    /**
     * Creates a builder to build a {@code Sam2Input} with the image.
     *
     * @param image the image
     * @return a new builder
     */
    public static Builder builder(Image image) {
        return new Builder(image);
    }

    /**
     * The builder for {@code Sam2Input}.
     */
    public static final class Builder {

        private Image image;
        private List<Point> points;
        private List<Integer> labels;
        private boolean visualize;

        Builder(Image image) {
            this.image = image;
            points = new ArrayList<>();
            labels = new ArrayList<>();
        }

        /**
         * Adds a point to the {@code Sam2Input}.
         *
         * @param x the X coordinate
         * @param y the Y coordinate
         * @return the builder
         */
        public Builder addPoint(int x, int y) {
            return addPoint(x, y, 1);
        }

        /**
         * Adds a point to the {@code Sam2Input}.
         *
         * @param x     the X coordinate
         * @param y     the Y coordinate
         * @param label the label of the point, 0 for background, 1 for foreground
         * @return the builder
         */
        public Builder addPoint(int x, int y, int label) {
            return addPoint(new Point(x, y), label);
        }

        /**
         * Adds a point to the {@code Sam2Input}.
         *
         * @param point the point on image
         * @param label the label of the point, 0 for background, 1 for foreground
         * @return the builder
         */
        public Builder addPoint(Point point, int label) {
            points.add(point);
            labels.add(label);
            return this;
        }

        /**
         * Adds a box area to the {@code Sam2Input}.
         *
         * @param x      the left coordinate
         * @param y      the top coordinate
         * @param right  the right coordinate
         * @param bottom the bottom coordinate
         * @return the builder
         */
        public Builder addBox(int x, int y, int right, int bottom) {
            addPoint(new Point(x, y), 2);
            addPoint(new Point(right, bottom), 3);
            return this;
        }

        /**
         * Sets the visualize for the {@code Sam2Input}.
         *
         * @return the builder
         */
        public Builder visualize() {
            visualize = true;
            return this;
        }

        /**
         * Builds the {@code Sam2Input}.
         *
         * @return the new {@code Sam2Input}
         */
        public Sam2Input build() {
            Point[] location = points.toArray(new Point[0]);
            int[] array = labels.stream().mapToInt(Integer::intValue).toArray();
            return new Sam2Input(image, location, array, visualize);
        }
    }

    private static final class Location {
        String type;
        int[] data;
        int label;

        public void setType(String type) {
            this.type = type;
        }

        public void setData(int[] data) {
            this.data = data;
        }

        public void setLabel(int label) {
            this.label = label;
        }
    }

    private static final class Prompt {

        @SerializedName("image_url")
        String image;

        Location[] prompt;
        boolean visualize;

        public void setImage(String image) {
            this.image = image;
        }

        public void setPrompt(Location[] prompt) {
            this.prompt = prompt;
        }

        public void setVisualize(boolean visualize) {
            this.visualize = visualize;
        }
    }
}