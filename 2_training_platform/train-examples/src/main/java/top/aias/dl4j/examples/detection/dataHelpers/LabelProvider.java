package top.aias.dl4j.examples.detection.dataHelpers;

import org.datavec.image.recordreader.objdetect.ImageObject;
import org.datavec.image.recordreader.objdetect.ImageObjectLabelProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public final class LabelProvider implements ImageObjectLabelProvider {

    private final Map<String, List<ImageObject>> labelMap;

    public LabelProvider(File dir) throws IOException {
        labelMap = new HashMap<String, List<ImageObject>>();

        for (String line : Files.readAllLines(Paths.get(dir.getPath() + "/_labels.csv"))) {
            String fileName = line.split(",")[5];

            int x1 = Integer.parseInt(line.split(",")[1]);
            int y1 = Integer.parseInt(line.split(",")[2]);
            int x2 = Integer.parseInt(line.split(",")[1]) + Integer.parseInt(line.split(",")[3]);
            int y2 = Integer.parseInt(line.split(",")[2]) + Integer.parseInt(line.split(",")[4]);

            ImageObject imageObject = new ImageObject(x1, y1, x2, y2, line.split(",")[0]);
            if (labelMap.containsKey(fileName))
                labelMap.get(fileName).add(imageObject);
            else {
                LinkedList<ImageObject> imageObjects = new LinkedList<ImageObject>();
                imageObjects.add(imageObject);
                labelMap.put(fileName, imageObjects);
            }
        }
    }

    @Override
    public List<ImageObject> getImageObjectsForPath(String path) {
        String filename = new File(path).getName();

        return labelMap.get(filename) != null ? labelMap.get(filename) : new LinkedList<>();
    }

    @Override
    public List<ImageObject> getImageObjectsForPath(URI uri) {
        return getImageObjectsForPath(uri.toString());
    }
}