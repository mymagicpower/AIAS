package me.aias.example.utils;

import java.io.*;

import javax.sound.sampled.*;

import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

/**
 * Sound format conversion utility class
 */
public class SoundUtils {
    // Audio type contants
    public static final AudioType MP3 = new AudioType("MPEG1L3", "MP3", "mp3");
    public static final AudioType WAV = new AudioType("ULAW", "WAVE", "wav");
    public static final AudioType WAV_PCM_SIGNED = new AudioType("PCM_SIGNED", "WAVE", "wav");

    private SoundUtils() {
    }

    /**
     * Converts a byte array of sound data to the given audio type, also returned as a byte array.
     */
    public static byte[] convertAsByteArray(byte[] source, AudioType targetType) {
        try {
            System.out.print("Converting byte array to AudioInputStream...");
            AudioInputStream ais = toStream(source, targetType);
            System.out.println("done.");
            System.out.print("Converting stream to new audio format...");
            ais = convertAsStream(ais, targetType);
            System.out.println("done.");
            System.out.print("Converting new stream to byte array...");
            byte[] target = toByteArray(ais, targetType);
            System.out.println("done.");
            return target;
        } catch (IOException ex) {
            throw new RuntimeException("Exception during audio conversion", ex);
        } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException("Exception during audio conversion", ex);
        }
    }

    /**
     * Converts an InputStream of sound data to the given audio type, returned as a byte array.
     */
    public static byte[] convertAsByteArray(InputStream is, AudioType targetType) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            ais = convertAsStream(ais, targetType);
            byte[] bytes = toByteArray(ais, targetType);
            return bytes;
        } catch (IOException ex) {
            throw new RuntimeException("Exception during audio conversion", ex);
        } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException("Exception during audio conversion", ex);
        }
    }

    /**
     * Converts an AudioInputStream to the indicated audio type, also returned as an AudioInputStream.
     */
    public static AudioInputStream convertAsStream(AudioInputStream sourceStream, AudioType targetType) {
        AudioFormat.Encoding targetEncoding = targetType.getEncoding();
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioInputStream targetStream = null;
        
        if (!AudioSystem.isConversionSupported(targetEncoding, sourceFormat)) {
            // Direct conversion not possible, trying with intermediate PCM format
            AudioFormat intermediateFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    sourceFormat.getSampleRate(),
                    16,
                    sourceFormat.getChannels(),
                    2 * sourceFormat.getChannels(), // frameSize
                    sourceFormat.getSampleRate(),
                    false);

            if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
// Intermediate conversion is supported
                sourceStream = AudioSystem.getAudioInputStream(intermediateFormat, sourceStream);
            }
        }

        targetStream = AudioSystem.getAudioInputStream(targetEncoding, sourceStream);

        if (targetStream == null) {
            throw new RuntimeException("Audio conversion not supported");
        }

        return targetStream;
    }

    /**
     * Converts a byte array to an AudioInputStream with the same audio format.
     */
    private static AudioInputStream toStream(byte[] bytes, AudioType targetType)
            throws IOException, UnsupportedAudioFileException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        AudioInputStream ais = AudioSystem.getAudioInputStream(bais);
        return ais;
    }

    /**
     * Converts an AudioInputStream to a byte array with the same audio format.
     */
    private static byte[] toByteArray(AudioInputStream ais, AudioType targetType) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AudioSystem.write(ais, targetType.getFileFormat(), baos);
        return baos.toByteArray();
    }

    /**
     * Append a wav file to another wav file
     */
    public static void appendStream(String wavFile1, String wavFile2, String destinationFile) {
        try (AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File(wavFile1));
             AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));
             AudioInputStream appendedFiles =
                     new AudioInputStream(
                             new SequenceInputStream(clip1, clip2),
                             clip1.getFormat(),
                             clip1.getFrameLength() + clip2.getFrameLength())) {

            AudioSystem.write(appendedFiles,
                    AudioFileFormat.Type.WAVE,
                    new File(destinationFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a wav file time length (seconds)
     */
    public static int getWavLengthSeconds(File sourceFile) throws Exception {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sourceFile)) {
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = sourceFile.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            // downcast to int
            return (int) durationInSeconds;
        }
    }

    /**
     * Create chop from a wav file
     */
    public static void createChop(File sourceFile, File destinationFile, int startSecond, int secondsToCopy) {
        try (AudioInputStream inputStream = AudioSystem.getAudioInputStream(sourceFile)) {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(sourceFile);
            AudioFormat format = fileFormat.getFormat();

            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = secondsToCopy * (int) format.getFrameRate() / 4;

            try (AudioInputStream shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy)) {
                AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Class representing an audio type, encapsulating an encoding and a file format.
     */
    public static class AudioType {
        private String encodingName;
        private String typeName;
        private String extension;

        public AudioType(String encodingName, String typeName, String extension) {
            this.encodingName = encodingName;
            this.typeName = typeName;
            this.extension = extension;
        }

        public AudioFormat.Encoding getEncoding() {
            return Encodings.getEncoding(encodingName);
        }

        public AudioFileFormat.Type getFileFormat() {
            return AudioFileTypes.getType(typeName, extension);
        }
    }
}